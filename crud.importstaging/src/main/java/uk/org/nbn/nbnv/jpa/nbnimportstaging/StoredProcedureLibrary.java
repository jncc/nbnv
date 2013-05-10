package uk.org.nbn.nbnv.jpa.nbnimportstaging;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.StoredProcedureCall;
import org.eclipse.persistence.sessions.Session;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class StoredProcedureLibrary {
    private EntityManager _em;
    public StoredProcedureLibrary(EntityManager em) {
        _em = em;
    }

    public void deleteTaxonObservationsAndRelatedRecords(String datasetKey) {

        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("import_DeleteTaxonObservationsAndRelatedRecords");
        call.addNamedArgument("datasetKey", "datasetKey");

        DataModifyQuery query = new DataModifyQuery();
        query.setCall(call);
        query.addArgument("datasetKey");

        List arguments = new ArrayList();
        arguments.add(datasetKey);

        Session session = getSession();
        session.executeQuery(query, arguments);
    }

    private Session getSession() {
        return ((EntityManagerImpl) _em).getActiveSession();
    }
}
