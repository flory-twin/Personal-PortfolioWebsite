package kflory.web.portfolio;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

//If annotation not present, fails; @Test MUST be used with @SpringBootTest unless one wants a standard JUnit test
// Note that this annotation "tells Spring Boot to look for a main configuration class (one with @SpringBootApplication, for instance) and use that to start a Spring application context." Not sure whether a context would be provided and/or any Spring testing infrastructure would pick up tests otherwise...
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArtControllerMethodsPointToArt {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void testGetReturnsNonNull() throws Exception {
		assertThat(
			this.restTemplate.getForObject(
				"http://localhost:" + port + "/art.html",
				String.class))
			.isNotNull();
	}
	
	@Test
	public void testGetDoesNotReturnMyNameInBody() throws Exception {
		assertThat(
			this.restTemplate.getForObject(
				"http://localhost:" + port + "/art.html",
				String.class))
			.contains("Kevin Flory");
	}
}
