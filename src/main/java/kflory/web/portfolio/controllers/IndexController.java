package kflory.web.portfolio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController
{
   @GetMapping(value={"/", "/index.html"})
   public ModelAndView returnIndexPage()
   {
      return new ModelAndView("index");
   }
}
