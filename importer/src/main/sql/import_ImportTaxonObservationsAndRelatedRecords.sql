USE [NBNCore]
GO

/****** Object:  StoredProcedure [dbo].[import_ImportTaxonObservationsAndRelatedRecords]    Script Date: 07/10/2013 12:07:44 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


--TODO -Handle attribution
--	-Attribute is specific to a dataset.
--	-Exisiting datasets 
--		-Get attribute map
--		-Insert new attributes
--	-New datasaets 
--		-Insert new attributs



-- =============================================
-- Author:  Felix Mason
-- Create date: 31/05/2013
-- Description: Import data from the import staging
-- tables into the core database
-- =============================================
alter PROCEDURE [dbo].[import_ImportTaxonObservationsAndRelatedRecords]
AS
BEGIN TRY
	BEGIN TRANSACTION


		DECLARE @DatasetKey char(8) = (SELECT TOP 1 [Key] FROM dbo.ImportDataset);
		
		IF @DatasetKey <> 'NewDst00' AND NOT EXISTS (SELECT TOP 1 ds.[key] FROM Dataset ds WHERE ds.[key] = @DatasetKey)
		BEGIN
			DECLARE @ErrorMsg varchar(20) = 'Unknown dataset key: ' + @DatasetKey
			RAISERROR(@ErrorMsg,16,1)
		END
		
		--DATASET IMPORT
		--Update Existing dataset
		UPDATE [dbo].[Dataset]
		   SET [datasetTypeKey] = ids.[datasetTypeKey]
			  ,[providerOrganisationKey] = ids.[providerOrganisationKey]
			  ,[title] = ids.[title]
			  ,[description] = ids.[description]
			  ,[providerKey] = ids.[providerKey]
			  ,[dataCaptureMethod] = ids.[dataCaptureMethod]
			  ,[purpose] = ids.[purpose]
			  ,[geographicalCoverage] = ids.[geographicalCoverage]
			  ,[temporalCoverage] = ids.[temporalCoverage]
			  ,[dataQuality] = ids.[dataQuality]
			  ,[additionalInformation] = ids.[additionalInformation]
			  ,[accessConstraints] = ids.[accessConstraints]
			  ,[useConstraints] = ids.[useConstraints]
			  ,[updateFrequencyCode] = ids.[updateFrequencyCode]
			  ,[dateUploaded] = ids.[dateUploaded]
			  ,[metadataLastEdited] = ids.[metadataLastEdited]
		 FROM dbo.Dataset ds
			INNER JOIN dbo.ImportDataset ids ON ds.[key] = ids.[key];
			
		--Insert New Datasets
		IF EXISTS(SELECT TOP 1 * FROM dbo.ImportDataset ids WHERE [key] = 'NewDst00')
		BEGIN
		
			DECLARE @newKey as TABLE ([key] char(8))
			
			INSERT INTO [dbo].[Dataset]
			   ([key]
			   ,[datasetTypeKey]
			   ,[providerOrganisationKey]
			   ,[title]
			   ,[description]
			   ,[providerKey]
			   ,[dataCaptureMethod]
			   ,[purpose]
			   ,[geographicalCoverage]
			   ,[temporalCoverage]
			   ,[dataQuality]
			   ,[additionalInformation]
			   ,[accessConstraints]
			   ,[useConstraints]
			   ,[updateFrequencyCode]
			   ,[dateUploaded]
			   ,[metadataLastEdited])
			   OUTPUT INSERTED.[key] INTO @newKey
			 SELECT top 1
				  dbo.fn_GetNewDatasetKey() AS newKey
				  ,datasetTypeKey
				  ,providerOrganisationKey
				  ,title
				  ,description
				  ,providerKey
				  ,dataCaptureMethod
				  ,purpose
				  ,geographicalCoverage
				  ,temporalCoverage
				  ,dataQuality
				  ,additionalInformation
				  ,accessConstraints
				  ,useConstraints
				  ,updateFrequencyCode
				  ,dateUploaded
				  ,metadataLastEdited 
			FROM dbo.ImportDataset ids
			WHERE ids.[key] = 'NewDst00'
			
			SET @DatasetKey = (SELECT TOP 1 [key] FROM @newKey)
		END
		
		--Update existing taxon datasets
		UPDATE [dbo].[TaxonDataset]
		   SET [publicResolutionID] = its.[publicResolutionID]
			  ,[allowRecordValidation] = its.allowRecordValidation
			  ,[publicAttribute] = its.publicAttribute
		FROM dbo.[TaxonDataset] ts
			INNER JOIN dbo.ImportTaxonDataset its on ts.datasetKey = its.datasetKey;
			
		--Import new taxon datasets
		INSERT INTO [dbo].[TaxonDataset]
			   ([datasetKey]
			   ,[publicResolutionID]
			   ,[allowRecordValidation]
			   ,[publicAttribute])
		SELECT @DatasetKey
			,ids.publicResolutionID
			,ids.allowRecordValidation
			,ids.publicAttribute
		FROM dbo.ImportTaxonDataset ids
		WHERE ids.datasetKey = 'NewDst00';
			
		
		--SURVEY IMPORT	
		--Import new surveys
		INSERT INTO dbo.Survey
			(datasetKey
			,providerKey
			,title)
		SELECT
			@DatasetKey
			,ims.providerKey
			,ims.title 
		FROM dbo.ImportSurvey ims
			LEFT JOIN dbo.Survey s ON s.datasetKey = @DatasetKey AND ims.providerKey = s.providerKey
		WHERE s.id IS NULL;
			
		--SAMPLE IMPORT
		--Insert new samples
		INSERT INTO dbo.Sample
			(surveyID
			,providerKey)
		SELECT 
			sv.id
			,ims.providerKey
		FROM dbo.ImportSample ims
			INNER JOIN dbo.ImportSurvey imsv ON ims.surveyID = imsv.id
			INNER JOIN dbo.Survey sv ON sv.datasetKey = @DatasetKey AND imsv.providerKey = sv.providerKey
			LEFT JOIN dbo.[Sample] s ON s.surveyID = sv.id AND s.providerKey = ims.providerKey
		WHERE s.id IS NULL;
		
		--SITE IMPORT
		--Import new sites - sites can match on provider key or name (with no key)
		INSERT INTO dbo.Site
			(datasetKey
			,providerKey
			,name)
		SELECT @DatasetKey
			,ims.providerKey
			,ims.name
		FROM dbo.ImportSite ims
			LEFT JOIN dbo.Site s on s.datasetKey = @DatasetKey AND (
				(ims.providerKey is null and s.providerKey is null and ims.name = s.name)
				OR  ims.providerKey = s.providerKey)
		WHERE s.id IS NULL;
		
		--RECORDER IMPORT
		--Insert new recorders and determiners
		INSERT INTO dbo.Recorder (name)
		SELECT
			ir.name
		FROM dbo.ImportRecorder ir 
			LEFT JOIN dbo.Recorder r ON ir.name = r.name
		WHERE r.id IS NULL;
		
		--TAXON OBSERVATION IMPORT
		MERGE [dbo].[TaxonObservation] as tob
		USING
			(SELECT sam.id AS [SampleId]
			  ,itob.[providerKey]
			  ,itob.[taxonVersionKey]
			  ,itob.[dateStart]
			  ,itob.[dateEnd]
			  ,itob.[dateTypeKey]
			  ,st.id AS SiteId
			  ,itob.[FeatureId]
			  ,itob.[absenceRecord]
			  ,itob.[sensitiveRecord]
			  ,rc.id AS [recorderID]
			  ,dt.id AS  [determinerID]
			  ,itob.id AS [ImportTaxonObservationId]
			FROM dbo.ImportTaxonObservation itob
				INNER JOIN dbo.ImportSample isam ON itob.sampleID = isam.id 
				INNER JOIN dbo.ImportSurvey isrv ON isam.surveyID = isrv.id
				INNER JOIN dbo.Survey srv ON srv.providerKey = isrv.providerKey
					AND srv.datasetKey = @DatasetKey
				INNER JOIN dbo.[Sample] sam ON sam.surveyID = srv.id 
					AND sam.providerKey = isam.providerKey
				LEFT JOIN dbo.ImportSite ist ON itob.siteID = ist.id
				LEFT JOIN dbo.[Site] st ON ((ist.providerKey = st.providerKey)
						OR (ist.providerKey IS NULL AND st.providerKey IS NULL AND st.name = ist.name))
					AND st.datasetKey = @DatasetKey
				LEFT JOIN dbo.ImportRecorder irc ON itob.recorderID = irc.id
				LEFT JOIN dbo.Recorder rc ON irc.name = rc.name
				LEFT JOIN dbo.ImportRecorder idt ON itob.determinerID = idt.id
				LEFT JOIN dbo.Recorder dt ON idt.name = dt.name) s
		ON s.SampleId = tob.SampleId AND s.providerKey = tob.providerKey
		WHEN NOT MATCHED THEN
			INSERT  
			([sampleID]
		   ,[providerKey]
		   ,[taxonVersionKey]
		   ,[dateStart]
		   ,[dateEnd]
		   ,[dateTypeKey]
		   ,[siteID]
		   ,[featureID]
		   ,[absenceRecord]
		   ,[sensitiveRecord]
		   ,[recorderID]
		   ,[determinerID])
		   VALUES
		   (s.SampleId
		   ,s.[providerKey]
		   ,s.[taxonVersionKey]
		   ,s.[dateStart]
		   ,s.[dateEnd]
		   ,s.[dateTypeKey]
		   ,s.[siteID]
		   ,s.[FeatureID]
		   ,s.[absenceRecord]
		   ,s.[sensitiveRecord]
		   ,s.[recorderID]
		   ,s.[determinerID])
		 WHEN MATCHED THEN
			UPDATE
		   SET [taxonVersionKey] = s.taxonVersionKey
			  ,[dateStart] = s.dateStart 
			  ,[dateEnd] = s.dateEnd 
			  ,[dateTypeKey] = s.dateTypeKey
			  ,[siteID] = s.siteID 
			  ,[featureID] = s.featureID 
			  ,[absenceRecord] = s.absenceRecord 
			  ,[sensitiveRecord] = s.sensitiveRecord 
			  ,[recorderID] = s.recorderID 
			  ,[determinerID] = s.determinerID;
		 
		
		MERGE [dbo].[TaxonObservationPublic] AS tobp
		USING
			(SELECT tob.id AS taxonObservationID
				,st.id AS siteId
				,itobp.featureID AS featureID
				,rr.id AS recorderID
				,rd.id AS determinerID
			FROM dbo.ImportTaxonObservationPublic itobp
				INNER JOIN dbo.ImportTaxonObservation itob ON itobp.taxonObservationID = itob.id
				INNER JOIN dbo.ImportSample isam ON itob.sampleID = isam.id 
				INNER JOIN dbo.ImportSurvey isrv ON isam.surveyID = isrv.id
				INNER JOIN dbo.Survey srv ON srv.providerKey = isrv.providerKey
					AND srv.datasetKey = @DatasetKey
				INNER JOIN dbo.[Sample] sam ON sam.surveyID = srv.id 
					AND sam.providerKey = isam.providerKey
				INNER JOIN dbo.TaxonObservation tob ON tob.sampleID = sam.id 
					AND tob.providerKey = itob.providerKey
				LEFT JOIN dbo.ImportSite ist ON itobp.siteID = ist.id
				LEFT JOIN dbo.[Site] st ON ((ist.providerKey = st.providerKey)
						OR (ist.providerKey IS NULL AND st.providerKey IS NULL AND st.name = ist.name))
					AND st.datasetKey = @DatasetKey
				LEFT JOIN dbo.ImportRecorder irr ON itobp.recorderID = irr.id
				LEFT JOIN dbo.Recorder rr ON irr.name = rr.name
				LEFT JOIN dbo.ImportRecorder ird ON itobp.determinerId = ird.id
				LEFT JOIN dbo.Recorder rd ON ird.name = rd.name ) s
		ON s.taxonObservationID = tobp.taxonObservationID
		WHEN NOT MATCHED THEN
			INSERT 
		   ([taxonObservationID]
		   ,[siteID]
		   ,[featureID]
		   ,[recorderID]
		   ,[determinerID])
			VALUES
			(s.taxonObservationID
			,s.siteID
			,s.featureID
			,s.recorderID
			,s.determinerID)
		WHEN MATCHED THEN
			UPDATE 
			   SET [siteID] = s.siteID 
				  ,[featureID] = s.featureID 
				  ,[recorderID] = s.recorderID 
				  ,[determinerID] = s.determinerID;
			         
         --INSERT ATTRIBUTES
         --todo - get attibute id's for current dataset
         
         
		INSERT INTO dbo.Attribute
		   ([label]
		   ,[description]
		   ,[storageLevelID]
		   ,[storageTypeID]
		   ,[gatewayAttributeID])
		SELECT ia.[label]
			  ,ia.[description]
			  ,ia.[storageLevelID]
			  ,ia.[storageTypeID]
			  ,ia.[gatewayAttributeID]
		FROM [dbo].[ImportAttribute] ia
			LEFT JOIN dbo.Attribute a ON ia.label = a.label
				AND ia.[storageLevelID] = a.[storageLevelID]
				AND ia.[storageTypeID] = a.[storageTypeID]
			--todo - and check id is not in set of attribute id's for current data set
		WHERE a.id IS NULL
		
			--todo output inserted id's in to set attribute id's for current dataset
			
		MERGE [dbo].[TaxonObservationAttribute] toa
		USING
			(SELECT tob.id AS observationID
				,a.id AS attributeID
				,itoba.decimalValue
				,itoba.enumValue
				,itoba.textValue
			FROM dbo.ImportAttribute ia
				INNER JOIN dbo.ImportTaxonObservationAttribute itoba ON ia.id = itoba.attributeID
				INNER JOIN dbo.ImportTaxonObservation itob ON itoba.observationID = itob.id
				INNER JOIN dbo.ImportSample isam ON itob.sampleID = isam.id 
				INNER JOIN dbo.ImportSurvey isrv ON isam.surveyID = isrv.id
				INNER JOIN dbo.Survey srv ON srv.providerKey = isrv.providerKey
					AND srv.datasetKey = @DatasetKey
				INNER JOIN dbo.[Sample] sam ON sam.surveyID = srv.id 
					AND sam.providerKey = isam.providerKey
				INNER JOIN dbo.TaxonObservation tob ON tob.sampleID = sam.id 
					AND tob.providerKey = itob.providerKey
				INNER JOIN dbo.Attribute a ON ia.label = a.label
					AND ia.[storageLevelID] = a.[storageLevelID]
					AND ia.[storageTypeID] = a.[storageTypeID]) s
					--todo: and id is in set of id's for current dataset
		ON s.observationID = toa.observationID AND s.attributeID = toa.attributeID
		WHEN NOT MATCHED THEN
			INSERT
           ([observationID]
           ,[attributeID]
           ,[decimalValue]
           ,[enumValue]
           ,[textValue])
			 VALUES
		   (s.observationID
		   ,s.attributeID
		   ,s.decimalValue
		   ,s.enumValue
		   ,s.textValue)
		WHEN MATCHED THEN
			UPDATE 
			   SET [observationID] = s.observationID
				  ,[attributeID] = s.attributeID
				  ,[decimalValue] = s.decimalValue
				  ,[enumValue] = s.enumValue
				  ,[textValue] = s.textValue;

		 
	COMMIT TRANSACTION
END TRY
BEGIN CATCH
	ROLLBACK TRANSACTION;
	
    --Report errors back to the client
	DECLARE @ErrorMessage NVARCHAR(4000);
	DECLARE @ErrorSeverity INT;
	DECLARE @ErrorState INT;

	SELECT 
		@ErrorMessage = ERROR_MESSAGE(),
		@ErrorSeverity = ERROR_SEVERITY(),
		@ErrorState = ERROR_STATE();
	
	RAISERROR (@ErrorMessage, -- Message text.
			@ErrorSeverity, -- Severity.
			@ErrorState -- State
			);
				
	RETURN;
END CATCH



GO


