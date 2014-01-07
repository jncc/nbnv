package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.model.PortalDownloadStatistic;
import uk.org.nbn.nbnv.api.dao.providers.*;
import uk.org.nbn.nbnv.api.model.User;

public interface PortalDownloadMapper {
    
    //@Select("SELECT * FROM GenericDownloadStatisticsData gdsd WHERE gdsd.datasetKey = #{datasetKey}  ORDER BY gdsd.downloadDate DESC")
    @SelectProvider(type = PortalDownloadProvider.class, method = "selectPortalDownloadsByDataset")
    public List<PortalDownloadStatistic> getPortalDownloadStatisticForDataset(
            @Param("dataset") String dataset, 
            @Param("startDate") String startDate, 
            @Param("endDate") String endDate);
    
    //@Select("SELECT * FROM GenericDownloadStatisticsData gdsd INNER JOIN DatasetAdministrator da ON da.datasetKey = gdsd.datasetKey WHERE da.userID = #{user.id} ORDER BY gdsd.downloadDate DESC")
    @SelectProvider(type = PortalDownloadProvider.class, method = "selectPortalDownloadsByUser")
    public List<PortalDownloadStatistic> getPortalDownloadStatisticForUser(
            @Param("user") User user,
            @Param("startDate") String startDate, 
            @Param("endDate") String endDate);
}