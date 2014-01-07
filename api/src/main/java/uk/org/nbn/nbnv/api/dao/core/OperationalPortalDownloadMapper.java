package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import uk.org.nbn.nbnv.api.model.User;

public interface OperationalPortalDownloadMapper {
    
    @Insert("INSERT INTO PortalDownload VALUES (#{user.id}, #{ip}, #{filterText}, GETDATE())")
    public int addPortalDownload(@Param("user") User user, String ip, String filterText);
    
    @Insert("INSERT INTO PortalDownloadStatistics VALUES (#{downloadID}, #{dataset}, #{downloaded}")
    public int addPortalDownloadStats(@Param("downloadID") int downloadID, @Param("dataset") String dataset, @Param("downloaded") int downloaded);
}
