/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbn.nbnv.nbnvimport.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Provides access to persistence manager objects for the NBNCore database
 * 
 * Uses the NBNCore-PU persistence unit defined in persistence.xml
 * @author  Paul Gilbertson
 * @version %I%, %G%
 * @since   1.0
 */
public class NBNCoreEMF {
    private static EntityManagerFactory _emf = Persistence.createEntityManagerFactory("NBNCore-PU");
    
    /**
     * Retrieves an active EntityManagerFactory for the NBNCore database using
     * the NBNCore-PU persistence unit
     * 
     * @return  Factory for creating EntityManagers
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        // If the EntityManagerFactory is closed, get a new one
        if (!_emf.isOpen())
            _emf = Persistence.createEntityManagerFactory("NBNCore-PU");
        
        return _emf;
    }

     /**
     * Retrieves an active EntityManager for the NBNCore database using
     * the NBNCore-PU persistence unit 
     * 
     * @return  EntityManager instance for using NBNCore database
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
}
