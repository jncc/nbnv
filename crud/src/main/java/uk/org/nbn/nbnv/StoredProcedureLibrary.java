package uk.org.nbn.nbnv;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.StoredProcedureCall;
import org.eclipse.persistence.queries.ValueReadQuery;
import org.eclipse.persistence.sessions.Session;
import uk.org.nbn.nbnv.jpa.nbncore.*;

import javax.persistence.EntityManager;
import org.eclipse.persistence.annotations.StoredProcedureParameter;
import org.eclipse.persistence.annotations.NamedStoredProcedureQuery;
import org.eclipse.persistence.annotations.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class StoredProcedureLibrary {
    private EntityManager _em;
    public StoredProcedureLibrary(EntityManager em) {
        _em = em;
    }
//
//    @NamedStoredProcedureQuery(
//            name="CREATE_FEATURE_PROCEDURE",
//            procedureName="import_CreateFeature",
//            returnsResultSet=false,
//            parameters={
//                    @StoredProcedureParameter(queryParameter="wkt",name="p1",direction=Direction.IN,type=Integer.class)
//            }
//    )
    public ImportFeature createFeature(String wgs84Wkt, String identifier) {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("import_CreateFeature");
        call.addNamedArgument("wkt", "wgs84wkt");
        call.addNamedArgument("identifier", "identifier");
        call.addNamedOutputArgument(
                "FeatureId",      // procedure parameter name
                "FeatureId",      // out argument field name
                Integer.class  // Java type corresponding to type returned by procedure
        );

        ValueReadQuery query = new ValueReadQuery();
        query.setCall(call);
        query.addArgument("wgs84wkt");   // input
        query.addArgument("identifier");

        List arguments = new ArrayList();
        arguments.add(wgs84Wkt);
        arguments.add(identifier);

        Session session = getSession();

        Integer featureId = (Integer) session.executeQuery(query, arguments);

        ImportFeature feature =  _em.find(ImportFeature.class,featureId);

        return feature;
    }

//    @featureID INT
//    , @gridRef VARCHAR(12)
//    , @resolutionID INT
//    , @projectionID INT
//    , @wkt VARCHAR(MAX)
//    , @gridSquareId INT OUT

    public ImportGridSquare createGridSquare(String gridRef, Resolution resolution, Projection projection, String wkt, ImportFeature wgs84Feature) {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("import_CreateGridSquare");

        call.addNamedArgument("gridRef", "gridRef");
        call.addNamedArgument("resolutionID", "resolutionID");
        call.addNamedArgument("projectionID", "projectionID");
        call.addNamedArgument("wkt", "wkt");
        call.addNamedArgument("featureID", "featureID");

        DataModifyQuery query = new DataModifyQuery();
        query.setCall(call);
        query.addArgument("gridRef");
        query.addArgument("resolutionID");
        query.addArgument("projectionID");
        query.addArgument("wkt");
        query.addArgument("featureID");

        Vector arguments = new Vector();
        arguments.add(gridRef);
        arguments.add(resolution.getId());
        arguments.add(projection.getId());
        arguments.add(wkt);
        arguments.add(wgs84Feature.getId());

        Session session = getSession();

        session.executeQuery(query, arguments);

        ImportGridSquare gridSquare =  _em.find(ImportGridSquare.class, gridRef);

        return gridSquare;
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

    public void importTaxonObservationsAndRelatedRecords() {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("import_ImportTaxonObservationsAndRelatedRecords");

        DataModifyQuery query = new DataModifyQuery();
        query.setCall(call);

        Session session = getSession();
        session.executeQuery(query);
    }

    private Session getSession() {
        return ((EntityManagerImpl) _em).getActiveSession();
    }
}
