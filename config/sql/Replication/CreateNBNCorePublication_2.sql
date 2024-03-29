use [NBNCore]
exec sp_replicationdboption @dbname = N'NBNCore', @optname = N'publish', @value = N'true'
GO
-- Adding the snapshot publication
exec sp_addpublication @publication = N'WarehouseSnapshot', @description = N'Snapshot publication of database ''NBNCore'' from Publisher ''NBN-MASTER''.', @repl_freq=N'snapshot', @retention = 0, @allow_push = N'true', @allow_pull = N'true', @allow_anonymous = N'true', @enabled_for_internet = N'false', @snapshot_in_defaultfolder = N'true', @compress_snapshot = N'false', @ftp_port = 21, @allow_subscription_copy = N'false', @add_to_active_directory = N'false', @status = N'active', @independent_agent = N'true', @immediate_sync = N'true', @allow_sync_tran = N'false', @allow_queued_tran = N'false', @allow_dts = N'false', @replicate_ddl = 1, @allow_initialize_from_backup = N'false', @enabled_for_p2p = N'false', @enabled_for_het_sub = N'false'
GO

exec sp_addpublication_snapshot @publication = N'WarehouseSnapshot', @frequency_type = 4, @frequency_interval = 1, @frequency_relative_interval = 1, @frequency_recurrence_factor = 0, @frequency_subday = 1, @frequency_subday_interval = 1, @active_start_time_of_day = 10000, @active_end_time_of_day = 235959, @active_start_date = 0, @active_end_date = 0, @job_login = null, @job_password = null, @publisher_security_mode = 0, @publisher_login = N'replagent', @publisher_password = N'AshesSpaceBeing2013'
GO


-- Adding the transactional publication
use [NBNCore]
exec sp_addpublication @publication = N'WarehouseTransaction', @description = N'Transactional publication of database ''NBNCore'' from Publisher ''NBN-MASTER''.', @sync_method = N'concurrent', @retention = 0, @allow_push = N'true', @allow_pull = N'true', @allow_anonymous = N'true', @enabled_for_internet = N'false', @snapshot_in_defaultfolder = N'true', @compress_snapshot = N'false', @ftp_port = 21, @allow_subscription_copy = N'false', @add_to_active_directory = N'false', @repl_freq = N'continuous', @status = N'active', @independent_agent = N'true', @immediate_sync = N'true', @allow_sync_tran = N'false', @allow_queued_tran = N'false', @allow_dts = N'false', @replicate_ddl = 1, @allow_initialize_from_backup = N'false', @enabled_for_p2p = N'false', @enabled_for_het_sub = N'false'
GO


exec sp_addpublication_snapshot @publication = N'WarehouseTransaction', @frequency_type = 4, @frequency_interval = 1, @frequency_relative_interval = 1, @frequency_recurrence_factor = 0, @frequency_subday = 1, @frequency_subday_interval = 1, @active_start_time_of_day = 10000, @active_end_time_of_day = 235959, @active_start_date = 0, @active_end_date = 0, @job_login = null, @job_password = null, @publisher_security_mode = 0, @publisher_login = N'replagent', @publisher_password = N'AshesSpaceBeing2013'


use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'AttributeData', @source_owner = N'dbo', @source_object = N'AttributeData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'AttributeData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'ContextLayerFeatureData', @source_owner = N'dbo', @source_object = N'ContextLayerFeatureData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'ContextLayerFeatureData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'DatasetAdministrator', @source_owner = N'dbo', @source_object = N'DatasetAdministrator', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetAdministrator', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetAttributeData', @source_owner = N'dbo', @source_object = N'DatasetAttributeData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'DatasetAttributeData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetContributingOrganisation', @source_owner = N'dbo', @source_object = N'DatasetContributingOrganisation', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetContributingOrganisation', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetData', @source_owner = N'dbo', @source_object = N'DatasetData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetDateTypeRecordCountData', @source_owner = N'dbo', @source_object = N'DatasetDateTypeRecordCountData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetDateTypeRecordCountData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetMappingData', @source_owner = N'dbo', @source_object = N'DatasetMappingData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'DatasetMappingData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetMappingDataEnhanced', @source_owner = N'dbo', @source_object = N'DatasetMappingDataEnhanced', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetMappingDataEnhanced', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetMappingDataPublic', @source_owner = N'dbo', @source_object = N'DatasetMappingDataPublic', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetMappingDataPublic', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetResolutionRecordCountData', @source_owner = N'dbo', @source_object = N'DatasetResolutionRecordCountData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetResolutionRecordCountData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DatasetYearRecordCountData', @source_owner = N'dbo', @source_object = N'DatasetYearRecordCountData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DatasetYearRecordCountData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DateType', @source_owner = N'dbo', @source_object = N'DateType', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DateType', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DesignationCategoryData', @source_owner = N'dbo', @source_object = N'DesignationCategoryData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DesignationCategoryData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DesignationData', @source_owner = N'dbo', @source_object = N'DesignationData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DesignationData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DesignationTaxonData', @source_owner = N'dbo', @source_object = N'DesignationTaxonData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'DesignationTaxonData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'DesignationTaxonNavigationGroupData', @source_owner = N'dbo', @source_object = N'DesignationTaxonNavigationGroupData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'DesignationTaxonNavigationGroupData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'FeatureContains', @source_owner = N'dbo', @source_object = N'FeatureContains', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'FeatureContains', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'FeatureData', @source_owner = N'dbo', @source_object = N'FeatureData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'FeatureData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'FeatureIdentifierData', @source_owner = N'dbo', @source_object = N'FeatureIdentifierData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'FeatureIdentifierData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'FeatureOverlaps', @source_owner = N'dbo', @source_object = N'FeatureOverlaps', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'FeatureOverlaps', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'GridSquareFeatureData', @source_owner = N'dbo', @source_object = N'GridSquareFeatureData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'GridSquareFeatureData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'GridTree', @source_owner = N'dbo', @source_object = N'GridTree', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'GridTree', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'HabitatDatasetData', @source_owner = N'dbo', @source_object = N'HabitatDatasetData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'HabitatDatasetData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'HabitatFeatureData', @source_owner = N'dbo', @source_object = N'HabitatFeatureData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'HabitatFeatureData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'HabitatFeatureFeatureData', @source_owner = N'dbo', @source_object = N'HabitatFeatureFeatureData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'HabitatFeatureFeatureData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'MappingData', @source_owner = N'dbo', @source_object = N'MappingData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'MappingData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'MappingDataEnhanced', @source_owner = N'dbo', @source_object = N'MappingDataEnhanced', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'MappingDataEnhanced', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'MappingDataPublic', @source_owner = N'dbo', @source_object = N'MappingDataPublic', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'MappingDataPublic', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'OrganisationAccessData', @source_owner = N'dbo', @source_object = N'OrganisationAccessData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'OrganisationAccessData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'OrganisationData', @source_owner = N'dbo', @source_object = N'OrganisationData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'OrganisationData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'OrganisationJoinRequestData', @source_owner = N'dbo', @source_object = N'OrganisationJoinRequestData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'OrganisationJoinRequestData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'OrganisationMembershipData', @source_owner = N'dbo', @source_object = N'OrganisationMembershipData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'OrganisationMembershipData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'OrganisationPublicTaxonObservationID', @source_owner = N'dbo', @source_object = N'OrganisationPublicTaxonObservationID', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'OrganisationPublicTaxonObservationID', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'OrganisationTaxonObservationAccess', @source_owner = N'dbo', @source_object = N'OrganisationTaxonObservationAccess', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'OrganisationTaxonObservationAccess', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'OrganisationTaxonObservationAttributeData', @source_owner = N'dbo', @source_object = N'OrganisationTaxonObservationAttributeData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'OrganisationTaxonObservationAttributeData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'OrganisationTaxonObservationData', @source_owner = N'dbo', @source_object = N'OrganisationTaxonObservationData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'OrganisationTaxonObservationData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'OrganisationTaxonObservationID', @source_owner = N'dbo', @source_object = N'OrganisationTaxonObservationID', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'OrganisationTaxonObservationID', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'OrganismData', @source_owner = N'dbo', @source_object = N'OrganismData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'OrganismData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'OrganismTaxonData', @source_owner = N'dbo', @source_object = N'OrganismTaxonData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'OrganismTaxonData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'RecorderData', @source_owner = N'dbo', @source_object = N'RecorderData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'RecorderData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'RecordingEntity', @source_owner = N'dbo', @source_object = N'RecordingEntity', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'RecordingEntity', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'Resolution', @source_owner = N'dbo', @source_object = N'Resolution', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'Resolution', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SampleData', @source_owner = N'dbo', @source_object = N'SampleData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SampleData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SiteBoundaryCategoryData', @source_owner = N'dbo', @source_object = N'SiteBoundaryCategoryData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SiteBoundaryCategoryData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SiteBoundaryData', @source_owner = N'dbo', @source_object = N'SiteBoundaryData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SiteBoundaryData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SiteBoundaryDatasetData', @source_owner = N'dbo', @source_object = N'SiteBoundaryDatasetData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SiteBoundaryDatasetData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SiteBoundaryFeatureData', @source_owner = N'dbo', @source_object = N'SiteBoundaryFeatureData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SiteBoundaryFeatureData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SiteData', @source_owner = N'dbo', @source_object = N'SiteData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SiteData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SurveyData', @source_owner = N'dbo', @source_object = N'SurveyData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SurveyData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'SurveySpeciesRecordCountData', @source_owner = N'dbo', @source_object = N'SurveySpeciesRecordCountData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'SurveySpeciesRecordCountData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonData', @source_owner = N'dbo', @source_object = N'TaxonData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonDatasetData', @source_owner = N'dbo', @source_object = N'TaxonDatasetData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonDatasetData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonDatasetTaxonData', @source_owner = N'dbo', @source_object = N'TaxonDatasetTaxonData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonDatasetTaxonData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonNavigationData', @source_owner = N'dbo', @source_object = N'TaxonNavigationData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonNavigationData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonNavigationGroupData', @source_owner = N'dbo', @source_object = N'TaxonNavigationGroupData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonNavigationGroupData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonNavigationGroupTaxonCount', @source_owner = N'dbo', @source_object = N'TaxonNavigationGroupTaxonCount', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonNavigationGroupTaxonCount', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonObservationAttributeData', @source_owner = N'dbo', @source_object = N'TaxonObservationAttributeData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonObservationAttributeData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonObservationData', @source_owner = N'dbo', @source_object = N'TaxonObservationData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'TaxonObservationData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonObservationDataEnhanced', @source_owner = N'dbo', @source_object = N'TaxonObservationDataEnhanced', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonObservationDataEnhanced', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonObservationDataPublic', @source_owner = N'dbo', @source_object = N'TaxonObservationDataPublic', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonObservationDataPublic', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonObservationFeatureData', @source_owner = N'dbo', @source_object = N'TaxonObservationFeatureData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonObservationFeatureData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonObservationPublicFeatureData', @source_owner = N'dbo', @source_object = N'TaxonObservationPublicFeatureData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonObservationPublicFeatureData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonOutputGroupData', @source_owner = N'dbo', @source_object = N'TaxonOutputGroupData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonOutputGroupData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonRecordCountData', @source_owner = N'dbo', @source_object = N'TaxonRecordCountData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonRecordCountData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonTaxonOutputGroupData', @source_owner = N'dbo', @source_object = N'TaxonTaxonOutputGroupData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonTaxonOutputGroupData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonTree', @source_owner = N'dbo', @source_object = N'TaxonTree', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonTree', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'TaxonWebLink', @source_owner = N'dbo', @source_object = N'TaxonWebLink', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'TaxonWebLink', @destination_owner = N'dbo', @status = 24, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'tfn_UserPublicID', @source_owner = N'dbo', @source_object = N'tfn_UserPublicID', @type = N'func schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000001, @destination_table = N'tfn_UserPublicID', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'UserAccessData', @source_owner = N'dbo', @source_object = N'UserAccessData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'UserAccessData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'UserAccessPosition', @source_owner = N'dbo', @source_object = N'UserAccessPosition', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'UserAccessPosition', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'UserData', @source_owner = N'dbo', @source_object = N'UserData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'UserData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'UserDatasetMappingData', @source_owner = N'dbo', @source_object = N'UserDatasetMappingData', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'UserDatasetMappingData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'UserMappingData', @source_owner = N'dbo', @source_object = N'UserMappingData', @type = N'view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000001, @destination_table = N'UserMappingData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'UserPublicID', @source_owner = N'dbo', @source_object = N'UserPublicID', @type = N'view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000001, @destination_table = N'UserPublicID', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'UserPublicTaxonObservationID', @source_owner = N'dbo', @source_object = N'UserPublicTaxonObservationID', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'UserPublicTaxonObservationID', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'UserTaxonObservationAccess', @source_owner = N'dbo', @source_object = N'UserTaxonObservationAccess', @type = N'logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'UserTaxonObservationAccess', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'UserTaxonObservationAttributeData', @source_owner = N'dbo', @source_object = N'UserTaxonObservationAttributeData', @type = N'view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000001, @destination_table = N'UserTaxonObservationAttributeData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'UserTaxonObservationData', @source_owner = N'dbo', @source_object = N'UserTaxonObservationData', @type = N'view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000001, @destination_table = N'UserTaxonObservationData', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseSnapshot', @article = N'UserTaxonObservationID', @source_owner = N'dbo', @source_object = N'UserTaxonObservationID', @type = N'indexed view schema only', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @destination_table = N'UserTaxonObservationID', @destination_owner = N'dbo', @status = 16
GO
use [NBNCore]
exec sp_addarticle @publication = N'WarehouseTransaction', @article = N'UserTokenAuthenticationData', @source_owner = N'dbo', @source_object = N'UserTokenAuthenticationData', @type = N'indexed view logbased', @description = N'', @creation_script = N'', @pre_creation_cmd = N'drop', @schema_option = 0x0000000008000051, @identityrangemanagementoption = N'none', @destination_table = N'UserTokenAuthenticationData', @destination_owner = N'dbo', @status = 16, @vertical_partition = N'false', @ins_cmd = N'SQL', @del_cmd = N'SQL', @upd_cmd = N'SQL'
GO




