package uk.org.nbn.nbnv;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/* Just ensure spring set up deployed ok
* */
@Controller
@RequestMapping("/hello")
public class HelloController {
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}

   /* @RequestMapping("/favicon.ico")
    String favicon() {
        return "forward:/app/img/favicon.ico";
    }*/
}