package uk.org.nbn.nbnv;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

public class PersistenceUtility {
    public EntityManagerFactory createEntityManagerFactory(Map settings)    {
        return Persistence.createEntityManagerFactory("NBNCore-PU", settings);
    }
}
