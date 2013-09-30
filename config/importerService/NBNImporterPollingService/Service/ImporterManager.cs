using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using log4net;

namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public enum ImportMode
    {
        Update
        , Append
    }

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

        public void RunImport(string importFile, ImportMode mode)
        {
            var p = new Process
                {
                    StartInfo =
                        {
                            UseShellExecute = false,
                            RedirectStandardOutput = true,
                            FileName = _configuration.JavaExePath,
                            Arguments = GetCommandLineArguments(_configuration.ImporterCommandLine, importFile, _configuration.ImporterLogFolder,
                            _configuration.TempFolder, mode)

                        }
                };
            
            _log.InfoFormat("Started processsing file {0}",importFile);

            _log.DebugFormat("Command line: {0} {1}", p.StartInfo.FileName, p.StartInfo.Arguments);
            
            p.Start();
            // Do not wait for the child process to exit before
            // reading to the end of its redirected stream.
            // p.WaitForExit();
            // Read the output stream first and then wait.
            _fileSystemManger.SaveStreamToFile(p.StandardOutput, _configuration.ImporterLogFolder, "ConsoleOutput.txt");

            p.WaitForExit();
            _log.InfoFormat("Finished processing file {0}", importFile);
        }

        private string GetCommandLineArguments(string rawCommandLine, string importFile, string logFolder, string tempFolder, ImportMode mode)
        {
            var cl = " -jar " + rawCommandLine;
            cl = cl.Replace("%importfile%", importFile);
            cl = cl.Replace("%logfolder%", logFolder);
            cl = cl.Replace("%tempfolder%", tempFolder);

            switch (mode)
            {
                case ImportMode.Update:
                    cl = cl.Replace("%mode%", "full");
                    break;
                case ImportMode.Append:
                    cl = cl.Replace("%mode%", "incremental");
                    break;
                default:
                    throw new Exception("Invalid import mode");
            }

            return cl;
        }
    }
}
