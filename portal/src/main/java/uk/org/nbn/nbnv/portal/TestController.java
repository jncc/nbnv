/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.portal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 *
 * @author Paul Gilbertson
 */
@Controller
public class TestController {
    @RequestMapping("/hello.html")
    public String hello(Model model) {
        model.addAttribute("message", "Hello world");
        return "hello";
    }
            
}
