package kflory.web.portfolio;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import kflory.web.portfolio.images.Image;
import kflory.web.portfolio.images.ImageCollection;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

//If annotation not present, fails; @Test MUST be used with @SpringBootTest unless one wants a standard JUnit test
// Note that this annotation "tells Spring Boot to look for a main configuration class (one with @SpringBootApplication, for instance) and use that to start a Spring application context." Not sure whether a context would be provided and/or any Spring testing infrastructure would pick up tests otherwise...
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArtControllerTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private ImageCollection ic;
	
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
	
	//Note: Also provides test case for what happens when image list has no contents (initialized by container)
	@Test
	public void testGetDoesNotReturnMyNameInBody() throws Exception {
		assertThat(
			this.restTemplate.getForObject(
				"http://localhost:" + port + "/art",
				String.class))
			.contains("Kevin Flory");
	}
	
	@Test
	public void testGetFromZero() throws Exception {
		ic.clear();
		ic.addImage(new Image("/art/20140516-2.jpeg"));
		ic.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ic.addImage(new Image("/art/20140526-wildwood-5.jpg"));

		//3 images: Still has 3x3, and if started <= index=2, some are onscreen.
		assertThat(
			this.restTemplate.getForObject(
				"http://localhost:" + port + "/art" + "?startingFrom=2",
				String.class))
			.contains("<div id=\"row3col3")
			.containsPattern("<img src=\"[A-Za-z1-9\\/]");
		
		//3 images: Still has 3x3, but if started higher then index=2, none onscreen.
		assertThat(
			this.restTemplate.getForObject(
				"http://localhost:" + port + "/art" + "?startingFrom=3",
				String.class))
			.contains("<div id=\"row3col3")
			.doesNotContainPattern("<img src=\"[A-Za-z1-9\\/]");
	}
}
