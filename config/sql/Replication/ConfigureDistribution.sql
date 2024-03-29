/****** Scripting replication configuration. Script Date: 20/08/2013 16:22:35 ******/
/****** Please Note: For security reasons, all password parameters were scripted with either NULL or an empty string. ******/

/****** Installing the server as a Distributor. Script Date: 20/08/2013 16:22:35 ******/
use master
exec sp_adddistributor @distributor = N'NBN-MASTER', @password = N'AshesSpaceBeing2013'
GO
exec sp_adddistributiondb @database = N'distribution', @data_folder = N'G:\Databases', @log_folder = N'G:\TransactionLogs', @log_file_size = 2, @min_distretention = 0, @max_distretention = 72, @history_retention = 48, @security_mode = 1
GO

use [distribution] 
if (not exists (select * from sysobjects where name = 'UIProperties' and type = 'U ')) 
	create table UIProperties(id int) 
if (exists (select * from ::fn_listextendedproperty('SnapshotFolder', 'user', 'dbo', 'table', 'UIProperties', null, null))) 
	EXEC sp_updateextendedproperty N'SnapshotFolder', N'G:\ReplData', 'user', dbo, 'table', 'UIProperties' 
else 
	EXEC sp_addextendedproperty N'SnapshotFolder', N'G:\ReplData', 'user', dbo, 'table', 'UIProperties'
GO

exec sp_adddistpublisher @publisher = N'NBN-MASTER', @distribution_db = N'distribution', @security_mode = 0, @login = N'replagent', @password = N'AshesSpaceBeing2013', @working_directory = N'G:\ReplData', @trusted = N'false', @thirdparty_flag = 0, @publisher_type = N'MSSQLSERVER'
GO
