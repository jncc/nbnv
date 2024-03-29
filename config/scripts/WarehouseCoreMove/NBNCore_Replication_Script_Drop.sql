
/****** Scripting removing replication objects. Script Date: 30/10/2014 10:26:27 ******/
/****** Please Note: For security reasons, all password parameters were scripted with either NULL or an empty string. ******/


-- Dropping the registered subscriber
exec sp_dropsubscriber @subscriber = N'NBN-B'
GO

-- Dropping the distribution publishers
exec sp_dropdistpublisher @publisher = N'NBN-A'
GO

-- Dropping the distribution databases
use master
exec sp_dropdistributiondb @database = N'distribution'
GO

/****** Uninstalling the server as a Distributor. Script Date: 30/10/2014 10:26:27 ******/
use master
exec sp_dropdistributor @no_checks = 1, @ignore_distributor = 1
GO
