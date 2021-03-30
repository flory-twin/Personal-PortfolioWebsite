package kflory.web.portfolio.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
	   
	@GetMapping(
		value = "/professional/resume",
		produces = MediaType.APPLICATION_PDF_VALUE
	)
	
	public ResponseEntity<byte[]> getResume(ServletRequest request) throws IOException, URISyntaxException {
		byte[] toReturn = {};
		Resource r = MavenDefeatingFileLoader.getResource("resources/static/KFlory.Resume.pdf");
		r.getInputStream().read(toReturn);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentLength(toReturn.length);
		/*
		responseHeaders.setLocation(
				new URL(request.getScheme(), request.getServerName(), "/professional&resumeDownloaded=true")
				.toURI());
				*/
		return new ResponseEntity<byte[]>(toReturn, responseHeaders, HttpStatus.OK);
	}
}
