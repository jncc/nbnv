/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uk.org.nbn.data.selenium;

import java.net.URL;
import static junit.framework.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
 *
 * @author Administrator
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:embedded-jetty-applicationContext.xml")
public class HomePageIT {
    @Autowired URL testServer;
    
    @Test public void loadHomePage() {
        WebDriver driver = new HtmlUnitDriver();
        
        driver.get(testServer.toString()); //Go to the deployed application root

        // Find the h1 element of the page
        WebElement element = driver.findElement(By.tagName("h1"));
        
        // Check the title of the page
        assertEquals("The title for the NBN Gateway is incorrect","NBN Gateway Wrong", element.getText());
    }
}
