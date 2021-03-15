package kflory.web.portfolio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
// NTS: 
public class IndexController
{
   @GetMapping(value={"/", "/index"})
   public String returnIndexPage()
   {
      return "index";
   }
}
