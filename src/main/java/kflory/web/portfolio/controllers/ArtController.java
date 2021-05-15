package kflory.web.portfolio.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
    // Note to self: My art JSP currently makes image requests by adding the Image's relative path to the base URL. And because of how I've constructed things, that translates directly to a servable resource.
    // Therefore, there's no need to provide a request handler for any image URL. Those end in a filename, and the DispatcherServlet routes such via SimpleUrlHanderMapping to the ResourceHttpRequestHandler, instead of using the RequestMappingHandlerMapping to map to a @RequestMapping- or @{Verb}Mapping-annotated controller method.  
}

