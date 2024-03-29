﻿using System;
using System.Diagnostics;
using System.IO;
using System.Threading.Tasks;
using log4net;

namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public class ImporterManager
    {
        private readonly ServiceConfiguration _configuration;
        private readonly FileSystemManger _fileSystemManger;
        private readonly ILog _log;

        public ImporterManager(ServiceConfiguration configuration, FileSystemManger fileSystemManger, ILog log)
        {
            _configuration = configuration;
            _fileSystemManger = fileSystemManger;
            _log = log;
        }

        public void RunImport(string importFile)
        {
            var p = new Process
                {
                    StartInfo =
                        {
                            UseShellExecute = false,
                            RedirectStandardOutput = true,
                            RedirectStandardError = true,
                            FileName = _configuration.JavaExePath,
                            Arguments = GetCommandLineArguments(_configuration.ImporterCommandLine, importFile, _configuration.ImporterLogFolder,
                            _configuration.TempFolder)

                        }
                };
            
            _log.InfoFormat("Started processsing file {0}",importFile);

            _log.DebugFormat("Command line: {0} {1}", p.StartInfo.FileName, p.StartInfo.Arguments);

            p.Start();

            using (Task importProcess = Task.Factory.StartNew(p.WaitForExit))
            using (Task saveStandardOutput = Task.Factory.StartNew(() => SaveOutput(p.StandardOutput, _configuration.ImporterLogFolder, "ConsoleOutput.txt", _log)))
            using (Task saveStandardError = Task.Factory.StartNew(() => SaveOutput(p.StandardError, _configuration.ImporterLogFolder, "ConsoleErrors.txt", _log)))
            {
                Task.WaitAll(importProcess, saveStandardError, saveStandardOutput);
            }

            _log.InfoFormat("Finished processing file {0}", importFile);
        }

        private static void SaveOutput(StreamReader source, string targetFolder, string targetFile, ILog log)
        {
            try
            {
                var fsm = new FileSystemManger();
                fsm.SaveStreamToFile(source, targetFolder, targetFile);
            }
            catch(Exception e)
            {
                log.ErrorFormat("Writing log {0} failed: {1}", targetFile, e.Message);
            }
        }

        private
            string GetCommandLineArguments(string rawCommandLine, string importFile, string logFolder, string tempFolder)
        {
            var cl = " -jar " + rawCommandLine;
            cl = cl.Replace("%importfile%", importFile);
            cl = cl.Replace("%logfolder%", logFolder);
            cl = cl.Replace("%tempfolder%", tempFolder);

            return cl;
        }
    }
}
