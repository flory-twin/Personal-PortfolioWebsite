package kflory.web.portfolio.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kflory.web.portfolio.images.Image;
import kflory.web.portfolio.images.ImageCollection;

@Controller
public class ArtController {
	
	@Autowired
	ImageCollection ic;
	
	@Value("${art.rowCount}")
	private int rowCount;
	
	@Value("${art.colCount}")
	private int colCount;
	

	@GetMapping("/art")
	public ModelAndView respondWithArtPage(
			@RequestParam(defaultValue="0") int startingFrom) 
	{
		//Temporary boilerplate so the site has something to display.
		ic.clear();
		ic.addImage(new Image("/art/20140516-2.jpeg"));
		ic.addImage(new Image("/art/20140516-1.v2.jpeg"));
		ic.addImage(new Image("/art/20140526-wildwood-5.jpg"));
		
		return new ModelAndView("art", 
				new ModelMap("imagePaths", ic.getImageArray(startingFrom, rowCount, colCount))
				.addAttribute("rowCount", rowCount)
				.addAttribute("colCount", colCount));
	}
}
