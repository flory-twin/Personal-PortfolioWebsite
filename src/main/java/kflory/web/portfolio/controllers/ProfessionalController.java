package kflory.web.portfolio.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import javax.servlet.ServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kflory.web.portfolio.utility.MavenDefeatingFileLoader;

@Controller
public class ProfessionalController {
	   @GetMapping("/professional")
	   public String returnIndexPage()
	   {
	      return "professional";
	   }

	//TODO Capture exceptions; replace default error page; do a redirect to error page per https://www.baeldung.com/spring-redirect-and-forward.
	@GetMapping(
		value = "/professional/KFlory.Resume.pdf",
		produces = MediaType.APPLICATION_PDF_VALUE
	)
	public ResponseEntity<byte[]> getResume(ServletRequest request) throws IOException, URISyntaxException {
		Resource r = MavenDefeatingFileLoader.getResource("static/KFlory.Resume.pdf");
		byte[] toReturn = Files.readAllBytes(r.getFile().toPath());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentDisposition(
				ContentDisposition.attachment()
				.filename("KFlory.Resume.pdf")
				.build());
		System.out.println(toReturn.length);
		
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.contentLength(r.contentLength())
				.contentType(MediaType.APPLICATION_PDF)
				.body(toReturn);
	}
}
