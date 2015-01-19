package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.*;

public interface DownloadMapper {
    
    // Dataset Download Notification Settings
    @Select("SELECT COUNT(*) FROM DatasetDownloadNotificationData WHERE userID = #{userID} AND datasetKey = #{datasetKey}")
    public boolean doesUserHaveDownloadNotificationsForDataset(
            @Param("userID") int userID,
            @Param("datasetKey") String datasetKey);
    
    @Select("SELECT * FROM DatasetDownloadNotificationData WHERE datasetKey = #{datasetKey}")
    public List<UserDownloadNotification> getUsersToNotifyForDatasetDownload(
            @Param("datasetKey") String datasetKey);
    
    @Select("SELECT * FROM DatasetDownloadNotificationData WHERE userID = #{userID}")
    public List<UserDownloadNotification> getNotifyingDatasetsForUser (
            @Param("userID") int userID);
}
