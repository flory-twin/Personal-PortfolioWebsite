package kflory.web.portfolio;


import static org.assertj.core.api.Assertions.assertThat;
//Strange. Spring Boot has xmlunit-matchers as a dependency JAR, yet I had to put an explicit entry for it into the POM to make this package visible.
//import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.XmlExpectationsHelper;
import org.springframework.test.util.XpathExpectationsHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import kflory.web.portfolio.controllers.ArtController;
import kflory.web.portfolio.images.Image;
import kflory.web.portfolio.images.ImageCollection;
import kflory.web.testUtilites.NamespaceContextImpl;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// Note that this annotation "tells Spring Boot to look for a main configuration class (one with @SpringBootApplication, for instance) and use that to start a Spring application context."
//  Without that, I don't get the interaction with the server which is necessary to verify final layout of JSP -> correct handoff of model.
//  Nor do I get all of the context autoconfiguration between the root context and the ServletContext.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Because the parameter test is dependent on how the following are configured, override them.
@TestPropertySource(properties = {"art.rowCount=3", "art.colCount=3"})
public class ArtControllerTest {
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGetReturnsNonNull() throws Exception {
		assertThat(
			this.restTemplate.getForObject(
				"http://localhost:" + port + "/art",
				String.class))
			.isNotNull();
	}
	
	@Test
	/* 
	 * Note to self: XPath-based testing turned out to be a pain. Spring provides two test util helpers, both of which don't work for me:
	 *  - XmlExpectationsHelper.assertNode(...) is producing configuration errors trying to locate http://www.w3.org/TR/xhtml11/DTD/xhtml-datatypes-1.mod (reporting java.IO.FileNotFoundException).
	 *  - And XpathExpectationsHelper.exists() (or similar) is never returning, though no error/exception occurs.
	 */
	public void testGetIndexParameter() throws Exception {
		// Since this test uses the full array of initializers/sets up all beans as requested, there should be an ImageCollection in the container somewhere.
		ImageCollection.clear();
		ImageCollection.addImage(new Image("/art/20140516-2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ImageCollection.addImage(new Image("/art/20140526-wildwood-5.jpg"));

		HashMap<String, String> ns = new HashMap<String, String>();
		//No, it's not intuitive naming. However, using the default namespace value (xmlns) as the prefix somehow breaks my tests.
		ns.put("derp", "http://www.w3.org/1999/xhtml");
		
		//Image array is 3x3 per test properties. If we set the starting count <= index=2, some are onscreen.
		String responseString = this.restTemplate.getForObject(
				"http://localhost:" + port + "/art" + "?startingFrom=2",
				String.class);
		
		String thumbnailsRoot = "//derp:div[@id = 'thumbnailArray']";
		String cells = "//derp:div[contains(@class, 'col')]";
		String nonEmptyImages = "//derp:img[@src != '']";
		String thumbnailCells = thumbnailsRoot + cells;
		String nonEmptyThumbnailImages = thumbnailsRoot + nonEmptyImages;
		
	    org.hamcrest.MatcherAssert.assertThat(
	    		responseString, 
	    		org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath("count(" + thumbnailCells + ")",
	    				equalTo("9"))
	    		.withNamespaceContext(ns));
	    org.hamcrest.MatcherAssert.assertThat(
	    		responseString, 
	    		org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath("count(" + nonEmptyThumbnailImages + ")",
	    				equalTo("1"))
	    		.withNamespaceContext(ns));

	    responseString = this.restTemplate.getForObject(
				"http://localhost:" + port + "/art" + "?startingFrom=3",
				String.class);
	    org.hamcrest.MatcherAssert.assertThat(
	    		responseString, 
	    		org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath("count(" + thumbnailCells + ")",
	    				equalTo("9"))
	    		.withNamespaceContext(ns));
	    org.hamcrest.MatcherAssert.assertThat(
	    		responseString, 
	    		org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath("count(" + nonEmptyThumbnailImages + ")",
	    				equalTo("0"))
	    		.withNamespaceContext(ns));
		

//		//3 images: Still has 3x3, but if started higher then index=2, none onscreen.
//		assertThat(
//				this.restTemplate.getForObject(
//					"http://localhost:" + port + "/art" + "?startingFrom=3",
//					String.class))
//				.contains("id=\"row3col3")
//				.doesNotContainPattern("img.*src=\"[A-Za-z1-9\\/]+");

//		byte[] responseBytes = this.restTemplate.getForObject(
//		"http://localhost:" + port + "/art" + "?startingFrom=2",
//		String.class).getBytes();
//XpathExpectationsHelper x = new XpathExpectationsHelper("//div[@id = 'row3col3']", null);
//x.assertNodeCount(responseBytes, null, 1);
////x.exists(responseBytes, null);

//XmlExpectationsHelper xml = new XmlExpectationsHelper();
//String responseFrom2 = this.restTemplate.getForObject(
//		"http://localhost:" + port + "/art" + "?startingFrom=2",
//		String.class);
//xml.assertNode(responseFrom2, 
//	hasXPath("//div[@id = 'row3col3']"));
//xml.assertNode(responseFrom2, 
//		hasXPath("//img[@src != ''"));
	}
}
