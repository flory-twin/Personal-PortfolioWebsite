package kflory.web.portfolio;

//Note to self: XPath assertions should have Spring support, but I can't make that work (see notes on testGetIndexParameter() below.)
//This test uses assertj's assertThat for 'normal' assertions and org.hamcrest.MatcherAssert.assertThat for xPath.
import static org.assertj.core.api.Assertions.assertThat;
//For use with Hamcrest assertThat.
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import kflory.web.portfolio.images.Image;
import kflory.web.portfolio.images.ImageCollection;

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
	//TODO: Mock 
	/**
	 * 
	 * @throws Exception
	 */
	public void testGetIndexParameter() throws Exception {
		// Since this test is a @SpringBootTest, the image collection will have been initialized during environment setup. Override its values with a minimal number of  
		ImageCollection.clear();
		ImageCollection.addImage(new Image("somePathString"));
		ImageCollection.addImage(new Image("somePathString"));
		ImageCollection.addImage(new Image("somePathString"));

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
		
		//Strange. Spring Boot has xmlunit-matchers as a dependency JAR, yet I had to put an explicit entry for it into the POM to make this package visible.
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
	}
}
