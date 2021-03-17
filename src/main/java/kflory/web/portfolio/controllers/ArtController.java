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
	private ImageCollection ic;
	
	@Value("${art.rowCount}")
	private int rowCount;
	
	@Value("${art.colCount}")
	private int colCount;
	

	@GetMapping("/art")
	public ModelAndView respondWithArtPage(
			@RequestParam(defaultValue="0") int startingFrom) 
	{
		return new ModelAndView("art", 
				new ModelMap("imagePaths", ic.getImageArray(startingFrom, rowCount, colCount))
				.addAttribute("rowCount", rowCount)
				.addAttribute("colCount", colCount));
	}
}
