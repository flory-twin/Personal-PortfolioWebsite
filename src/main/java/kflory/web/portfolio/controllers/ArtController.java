package kflory.web.portfolio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ArtController {
	@GetMapping("art.html")
	public ModelAndView respondWithArtPage() 
	{
		return new ModelAndView("art");
	}
}
