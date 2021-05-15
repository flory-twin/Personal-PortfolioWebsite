package kflory.web.portfolio.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

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
	
    //@Autowired
    //HttpSession session;
    
	@GetMapping("/art")
	public ModelAndView respondWithArtPage(
			@RequestParam(defaultValue="0") int startingFrom) 
	{
	    // Don't even try to fulfill requests for negative indexes.
	    if (startingFrom < 0) 
	    {
	        startingFrom = 0;
	    }
		return new ModelAndView("art", 
				new ModelMap("imagePaths", ic.getImageArray(startingFrom, rowCount, colCount))
				.addAttribute("rowCount", rowCount)
				.addAttribute("colCount", colCount)
				.addAttribute("pageSize", rowCount * colCount)
				.addAttribute("startingFrom", startingFrom)
				.addAttribute("lastIndex", ic.count()-1 ));
	}
	
	// Model becomes separate from view?
	// Model wld need paths to thumbs and paths to full. Cld make sense to use client-end code to break down larger send.
	//pt of fact, load time if including model always at max? it IS just a big string
	// ...wait. Think I'm confusing 'JSP makes page containing image path literals, wh can be fetched'
	// specify lazy load for images
	// image thumbnail 'pointer' list
	// full image (handled always by html? but will need responsebody for img download
}
