package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import uk.org.nbn.nbnv.api.model.DatasetLicence;

public interface DatasetLicenceMapper {

    @Select("SELECT CASE WHEN img IS NULL THEN 0 ELSE 1 END as hasImg, id, "
            + "abbreviation, name, summary, href FROM DatasetLicenceData WHERE "
            + "id = #{id}")
    DatasetLicence getDatasetLicenceByID(@Param("id") int id);    
    
    @Select("SELECT CASE WHEN img IS NULL THEN 0 ELSE 1 END as hasImg, id, "
            + "abbreviation, name, summary, href FROM DatasetLicenceData WHERE "
            + "abbreviation = #{abbrv}")
    DatasetLicence getDatasetLicenceByAbbrv(@Param("abbrv") String abbrv);
    
    @Select("SELECT CASE WHEN img IS NULL THEN 0 ELSE 1 END as hasImg, id, "
            + "abbreviation, name, summary, href FROM DatasetLicenceData")
    List<DatasetLicence> selectAllDatasetLicences();
    
    @Select("SELECT img FROM DatasetLicenceData WHERE id = #{id}")
    Object selectImgByDatasetLicenceDataID(int id);
}
