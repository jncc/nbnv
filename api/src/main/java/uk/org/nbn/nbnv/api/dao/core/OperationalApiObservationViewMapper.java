package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import uk.org.nbn.nbnv.api.model.ApiObservationView;

public interface OperationalApiObservationViewMapper {
    
    @Insert("INSERT INTO ApiObservationView (userID, ip, filterText, viewTime, viewed) VALUES (#{userID}, #{ip}, #{filterText}, GETDATE(), #{viewed})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    public int addAPIObservationView(ApiObservationView view);
    
    @Insert("INSERT INTO ApiObservationViewStatistics (viewID, datasetKey, recordCount) VALUES (#{viewID}, #{dataset}, #{viewed})")
    public int addAPIObservationViewStats(@Param("viewID") int viewID, @Param("dataset") String dataset, @Param("viewed") int viewed);
}
