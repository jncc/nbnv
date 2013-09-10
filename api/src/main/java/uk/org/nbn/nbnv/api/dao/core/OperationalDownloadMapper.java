package uk.org.nbn.nbnv.api.dao.core;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface OperationalDownloadMapper {
    
    // Dataset Download Notification Settings
    @Insert("INSERT INTO DatasetDownloadNotification (userId, datasetKey) VALUES (#{userID}, #{datasetKey})")
    public boolean addUserNotificationForDatasetDownload(
            @Param("userID") int userID, 
            @Param("datasetKey") String datasetKey);
    
    @Delete("DELETE FROM DatasetDownloadNotification WHERE userID = #{userID} AND datasetKey = #{datasetKey}")
    public boolean removeUserNotificationForDownload(
            @Param("userID") int userID, 
            @Param("datasetKey") String datasetKey);
}
