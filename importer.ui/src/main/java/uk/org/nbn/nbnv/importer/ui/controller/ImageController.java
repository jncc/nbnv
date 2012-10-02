/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.nbn.nbnv.importer.ui.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.org.nbn.nbnv.importer.ui.util.DatabaseConnection;
import uk.org.nbn.nbnv.jpa.nbncore.Organisation;

/**
 *
 * @author Matt Debont
 */
@Controller
public class ImageController {
    
    @RequestMapping(value="/image/{id}/{type}")
    public byte[] returnImg(@PathVariable String id, @PathVariable String type) {
        try {
            EntityManager em = DatabaseConnection.getInstance().createEntityManager();
            Query q = em.createNamedQuery("Organisation.findById");
            q.setParameter("id", id);
            Organisation org = (Organisation) q.getSingleResult();        

            if (type.equals("large")) {
                return org.getLogo();
            } else if (type.equals("small")) {
                return org.getLogoSmall();
            }
        } catch (NoResultException ex) {
            Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    @RequestMapping(value="/imageBase/{id}")
    public byte[] getImgBase64(@PathVariable String id) {
        Base64 in = new Base64(true);
        return in.decode(id);
    }
}
