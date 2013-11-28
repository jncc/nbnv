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
    public Feature createFeature(String wgs84Wkt, String identifier) {
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

        Feature feature =  _em.find(Feature.class,featureId);

        return feature;
    }

//    @featureID INT
//    , @gridRef VARCHAR(12)
//    , @resolutionID INT
//    , @projectionID INT
//    , @wkt VARCHAR(MAX)
//    , @gridSquareId INT OUT

    public GridSquare createGridSquare(String gridRef, Resolution resolution, Projection projection, String wkt, Feature wgs84Feature) {
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

        GridSquare gridSquare =  _em.find(GridSquare.class, gridRef);

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

    public void clearImportStagingTables() {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("import_ClearImportStagingTables");

        DataModifyQuery query = new DataModifyQuery();
        query.setCall(call);

        Session session = getSession();
        session.executeQuery(query);
    }

    public String importTaxonObservationsAndRelatedRecords() throws Exception {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("import_ImportTaxonObservationsAndRelatedRecords");
        call.addNamedOutputArgument(
                "Result",      // procedure parameter name
                "Result",      // out argument field name
                String.class  // Java type corresponding to type returned by procedure
        );

        ValueReadQuery query = new ValueReadQuery();
        query.setCall(call);

        Session session = getSession();
//        int i = session.executeNonSelectingCall(call);
        String result = (String) session.executeQuery(query);

        //result will begin with "Error" if the sproc has failed
        if (result.startsWith("Error")) throw new Exception("The import_ImportTaxonObservationsAndRelatedRecords stored procedure failed. Error: " + result);

        //else result will be the dataset key.
        return result;
    }

    public void setDatasetPublic(String datasetKey) throws Exception {
        StoredProcedureCall call = new StoredProcedureCall();
        call.setProcedureName("import_SetDatasetPublic");
        call.addNamedArgument("datasetKey", "datasetKey");
        call.addNamedOutputArgument(
                "result",      // procedure parameter name
                "result",      // out argument field name
                Integer.class  // Java type corresponding to type returned by procedure
        );

        ValueReadQuery query = new ValueReadQuery();
        query.setCall(call);
        query.addArgument("datasetKey");

        List arguments = new ArrayList();
        arguments.add(datasetKey);

        Session session = getSession();
        Integer result = (Integer) session.executeQuery(query, arguments);

        //result will begin with "Error" if the sproc has failed
        if (result > 0 ) throw new Exception("The import_SetDatasetPublic sproc failed");
    }

    private Session getSession() {
        return ((EntityManagerImpl) _em).getActiveSession();
    }
}
