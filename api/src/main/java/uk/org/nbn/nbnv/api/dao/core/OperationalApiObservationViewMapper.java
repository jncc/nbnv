package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import uk.org.nbn.nbnv.api.model.User;

public interface OperationalApiObservationViewMapper {
    
    @Insert("INSERT INTO ApiObservationView VALUES (#{user.id}, #{ip}, #{filterText}, GETDATE())")
    @SelectKey(statement="call identity()", keyProperty="id", before=false, resultType=int.class)
    public int addAPIObservationView(@Param("user") User user, String ip, String filterText);
    
    @Insert("INSERT INTO ApiObservationViewStatistics VALUES (#{downloadID}, #{dataset}, #{downloaded}")
    public int addAPIObservationViewStats(@Param("downloadID") int downloadID, @Param("dataset") String dataset, @Param("downloaded") int downloaded);
}
