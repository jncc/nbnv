package uk.org.nbn.nbnv.api.dao.warehouse;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import uk.org.nbn.nbnv.api.dao.providers.TaxonObservationProvider;
import uk.org.nbn.nbnv.api.model.*;

public interface DatasetMapper {

	@Select("SELECT * FROM DatasetData ORDER BY title")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})
	List<Dataset> selectAll();

	@Select("SELECT TOP 10 * from DatasetData order by dateUploaded desc")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})			
	List<Dataset> getLatestUploaded();

	@Select("SELECT * FROM DatasetData WHERE typeName = 'Taxon' ORDER BY title")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})			
	List<TaxonDataset> selectAllTaxonDatasets();

	@Select("SELECT * FROM DatasetData WHERE DatasetData.\"key\" = #{key}")
	@Results(value = {
		@Result(property = "organisation", column = "organisationID", javaType = Organisation.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.OrganisationMapper.selectByID")),
		@Result(property = "organisationID", column = "organisationID"),
		@Result(property = "key", column = "key"),
		@Result(property = "contributingOrganisations", column = "key", javaType = List.class, many =
				@Many(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetContributingOrganisationMapper.selectOrganisationsByDataset")),
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})
	Dataset selectByDatasetKey(String key);

	@Select("SELECT *, tdd.label publicResolution FROM DatasetData dd INNER JOIN TaxonDatasetData tdd ON dd.\"key\" = tdd.datasetKey WHERE dd.\"key\" = #{key}")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})	                
	TaxonDataset selectByIDProviderNotInstantiated(String key);

	@Select("SELECT * FROM DatasetData WHERE organisationID = #{organisaionID} ORDER BY title")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})			
	List<Dataset> selectByOrganisationID(int organisationID);

	@Select("SELECT dd.* FROM DatasetData dd INNER JOIN DatasetContributingOrganisation dco ON dco.datasetKey = dd.\"key\" WHERE dco.organisationID = #{organisaionID} ORDER BY dd.title")
        @Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})	
	List<Dataset> selectContributedByOrganisationID(int organisationID);

	@Select("SELECT *, tdd.label publicResolution FROM DatasetData dd INNER JOIN TaxonDatasetData tdd ON dd.\"key\" = tdd.datasetKey WHERE dd.\"key\" = #{key}")
	@Results(value = {
		@Result(property = "speciesCount", column = "key", javaType = java.lang.Integer.class, one =
				@One(select = "selectSpeciesCountByDatasetKey")),
		@Result(property = "key", column = "key"),
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))                
	})
	TaxonDataset selectTaxonDatasetByID(String key);

	@Select("SELECT year(startDate) year, COUNT_BIG(*) recordCount FROM TaxonObservationData WHERE datasetKey = #{key} AND fullVersion = 1 AND YEAR(startDate) <> 0 GROUP BY datasetKey, year(startDate) ORDER BY year")
	List<YearStats> selectRecordsPerYear(String key);

	@Select("SELECT * FROM DatasetDateTypeRecordCountData WHERE datasetKey = #{key}")
	List<DateTypeStats> selectRecordCountPerDateTypeByDatasetKey(String key);

	@Select("SELECT count(*) speciesCount FROM TaxonDatasetTaxonData WHERE datasetKey = #{key}")
	Integer selectSpeciesCountByDatasetKey(String key);

	@SelectProvider(type = TaxonObservationProvider.class, method = "filteredSelectDatasetsProviderNotInstantiated")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})			
	List<Dataset> selectDatasetsInDesignationViewableByUser(@Param("user") User user, @Param("designation") String designationKey);

	@SelectProvider(type = TaxonObservationProvider.class, method = "filteredSelectDatasetsProviderNotInstantiated")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})			
	List<Dataset> selectDatasetsForTaxonViewableByUser(@Param("user") User user, @Param("ptvk") String pTaxonVersionKey);

	@Select("SELECT owner, filterText FROM UserAccessPosition uap WHERE datasetKey = #{datasetKey} AND userID = #{userID}")
	List<AccessPosition> getDatasetAccessPositions(@Param("datasetKey") String datasetKey, @Param("userID") int userID);

	@Select("SELECT drrcd.datasetKey, drrcd.count, r.label, r.id AS resolutionID FROM DatasetResolutionRecordCountData drrcd LEFT JOIN Resolution r ON r.label = drrcd.label WHERE datasetKey = #{datasetKey}")
	List<DatasetResolutionRecordCount> getResolutionData(@Param("datasetKey") String datasetKey);
	
	@Select("SELECT * FROM DatasetData dd INNER JOIN DatasetLicence dld ON dd.licenceID = dld.id WHERE dld.abbreviation = #{licenceAbbrv} ORDER BY dd.title")
	@Results(value = {
		@Result(property = "licenceID", column = "licenceID"),
		@Result(property = "datasetLicence", column = "licenceID", javaType = DatasetLicence.class, one =
				@One(select = "uk.org.nbn.nbnv.api.dao.warehouse.DatasetLicenceMapper.getDatasetLicenceByID"))
	})
	List<Dataset> getDatasetsByLicence(@Param("licenceAbbrv") String licenceAbbrv);
}
