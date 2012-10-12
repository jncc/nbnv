package uk.org.nbn.nbnv;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.StoredProcedureCall;
import org.eclipse.persistence.queries.ValueReadQuery;
import org.eclipse.persistence.sessions.Session;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class SpatialQueries {
    private EntityManager _em;
    public SpatialQueries(EntityManager em) {
        _em = em;
    }

    public String getGridProjectionForWGS84wkt(String wkt) {

        StoredProcedureCall spcall = new StoredProcedureCall();
        spcall.setProcedureName("import_getGridForWKT");
        spcall.addNamedArgument("wkt", "wkt");
        spcall.addNamedOutputArgument(
                "projection",      // procedure parameter name
                "projection",      // out argument field name
                String.class  // Java type corresponding to type returned by procedure
        );

        ValueReadQuery query = new ValueReadQuery();
        query.setCall(spcall);
        query.addArgument("wkt");

        List arguments = new ArrayList();
        arguments.add(wkt);

        Session session = getSession();

        String projection = (String) session.executeQuery(query, arguments);

        return projection;
    }

    private Session getSession() {
        return ((EntityManagerImpl) _em).getActiveSession();
    }
}
