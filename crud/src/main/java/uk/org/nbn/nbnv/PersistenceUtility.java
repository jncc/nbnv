package uk.org.nbn.nbnv;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtility {
    public EntityManagerFactory createEntityManagerFactory()    {
        return Persistence.createEntityManagerFactory("NBNCore-PU");
    }
}
