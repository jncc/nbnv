
/****** Object:  StoredProcedure [dbo].[import_DeleteTaxonObservationsAndRelatedRecords]    Script Date: 07/12/2013 09:40:45 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



------------------------------

-- =============================================
-- Author:		Pete
-- Create date: 20121026
-- Description:	Used by the importer for re-importing existing datasets - the way we do this is by updating the
-- Dataset/TaxonDataset records but completely deleting the associated observation records and re-importing.
-- =============================================
CREATE PROCEDURE [dbo].[import_DeleteTaxonObservationsAndRelatedRecords] 
	@datasetKey char(8)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	IF NOT EXISTS(SELECT TOP 1 * FROM dbo.Dataset d WHERE d.[key] = @datasetKey) RETURN;

	DELETE FROM dbo.[TaxonObservationAttribute] WHERE observationID IN (
		SELECT o.id
		FROM dbo.[Survey] r 
		INNER JOIN dbo.[Sample] s ON s.surveyID = r.id 
		INNER JOIN dbo.[TaxonObservation] o on o.sampleID = s.id
		WHERE r.datasetKey = @datasetKey
	);
	
	DELETE FROM [TaxonObservationPublic] where taxonObservationID in (
		SELECT o.id
		FROM dbo.[Survey] r 
		INNER JOIN dbo.[Sample] s ON s.surveyID = r.id 
		INNER JOIN dbo.[TaxonObservation] o on o.sampleID = s.id
		WHERE r.datasetKey = @datasetKey
	);

	DELETE FROM [TaxonObservation] WHERE sampleID IN (
		SELECT s.id
		FROM [Survey] r 
		INNER JOIN [Sample] s ON s.surveyID = r.id
		WHERE r.datasetKey = @datasetKey
	);

	DELETE FROM [Sample] WHERE surveyID IN (
		SELECT id FROM [Survey] WHERE datasetKey = @datasetKey
	);
	
	DELETE FROM [Survey] WHERE datasetKey = @datasetKey;

	DELETE FROM [Site] WHERE datasetKey = @datasetKey;
END



GO


