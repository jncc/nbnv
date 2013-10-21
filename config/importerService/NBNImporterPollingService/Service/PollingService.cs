﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using log4net;
using Timer = System.Timers.Timer;

namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public class PollingService
    {

        private Timer _timer;
        private readonly ServiceConfigurationManager _configManager;
        private ServiceConfiguration _config;
        private ILog _log;

        public readonly static object ThreadLock = new object();

        public PollingService()
        {
            _configManager = new ServiceConfigurationManager();
            _log = LogManager.GetLogger(typeof (PollingService));
        }

        public void Start()
        {
            _config = _configManager.GetServiceConfiguration();
            _timer = new Timer(_config.PollingIntervalInMilliseconds) {AutoReset = true, Enabled = true};
            _timer.Elapsed += (sender, eventArgs) => RunImport(_config, _log);
            _timer.Start();
        }


        public void Stop()
        {
            _timer.Stop();
        }

        private void RunImport(ServiceConfiguration config, ILog log)
        {

            if (Monitor.TryEnter(ThreadLock))
            {
                try
                {
                    ImportFromFolder(config, log, config.NewOrReplaceFolder, ImportMode.NewOrReplace);
                    ImportFromFolder(config, log, config.AppendsFolder, ImportMode.Append);
                }
                catch (Exception e)
                {
                    //timer.Elapsed method suppresses error propogation.
                    log.Error(e.Message, e);
                }
                finally
                {
                    Monitor.Exit(ThreadLock);
                }
            }
  
        }

        private void ImportFromFolder(ServiceConfiguration config, ILog log, string sourceFolder, ImportMode mode)
        {
            var fileSystemManager = new FileSystemManger();
            var importerManager = new ImporterManager(config, fileSystemManager, log);

            var files = fileSystemManager.GetListOfFiles(sourceFolder, "*.zip");

            foreach (var f in files)
            {
                //  clear log folder down
                fileSystemManager.ClearFilesInFolder(config.ImporterLogFolder);
                //  run importer
                importerManager.RunImport(f.FullName, mode);

                //  create results sub folder
                var folderForResults = fileSystemManager.CreateSubFolderForResults(config.ResultFolder, f, mode);

                //  move file from source folder to results folder
                fileSystemManager.MoveFileToLocation(f, folderForResults);

                //  move contents of log folder to results sub folder.
                fileSystemManager.MoveFilesToFolder(config.ImporterLogFolder, folderForResults, "*");
            }
        }
    }
}
