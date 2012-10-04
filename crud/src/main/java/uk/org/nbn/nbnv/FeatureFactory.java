package uk.org.nbn.nbnv;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.StoredProcedureCall;
import org.eclipse.persistence.queries.ValueReadQuery;
import org.eclipse.persistence.sessions.Session;
import uk.org.nbn.nbnv.jpa.nbncore.Feature;

import javax.persistence.EntityManager;
import org.eclipse.persistence.annotations.StoredProcedureParameter;
import org.eclipse.persistence.annotations.NamedStoredProcedureQuery;
import org.eclipse.persistence.annotations.Direction;
import uk.org.nbn.nbnv.jpa.nbncore.GridSquare;
import uk.org.nbn.nbnv.jpa.nbncore.Projection;
import uk.org.nbn.nbnv.jpa.nbncore.Resolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: felix mason
 * Date: 03/10/12
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class FeatureFactory {
    private EntityManager _em;
    public FeatureFactory(EntityManager em) {
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
    public Feature createFeature(String wgs84Wkt) {
        StoredProcedureCall spcall = new StoredProcedureCall();
        spcall.setProcedureName("import_CreateFeature");
        spcall.addNamedArgument("wkt", "wgs84wkt");
        spcall.addNamedOutputArgument(
                "FeatureId",      // procedure parameter name
                "FeatureId",      // out argument field name
                Integer.class  // Java type corresponding to type returned by procedure
        );

        ValueReadQuery query = new ValueReadQuery();
        query.setCall(spcall);
        query.addArgument("wgs84wkt");   // input

        List arguments = new ArrayList();
        arguments.add(wgs84Wkt);

        Session session = ((EntityManagerImpl) _em).getActiveSession();

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
        StoredProcedureCall spcall = new StoredProcedureCall();
        spcall.setProcedureName("import_CreateGridSquare");

        spcall.addNamedArgument("gridRef", "gridRef");
        spcall.addNamedArgument("resolutionID", "resolutionID");
        spcall.addNamedArgument("projectionID", "projectionID");
        spcall.addNamedArgument("wkt", "wkt");
        spcall.addNamedArgument("featureID", "featureID");

        DataModifyQuery query = new DataModifyQuery();
        query.setCall(spcall);
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

        Session session = ((EntityManagerImpl) _em).getActiveSession();

        session.executeQuery(query, arguments);

        GridSquare gridSquare =  _em.find(GridSquare.class, gridRef);

        return gridSquare;
    }
}
