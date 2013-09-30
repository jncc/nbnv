using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Topshelf;
using Topshelf.Runtime;
using log4net;
using uk.org.nbn.nbnv.ImporterPollingService.Service;


namespace uk.org.nbn.nbnv.ImporterPollingService
{
    class Program
    {

        private static ServiceConfiguration _config;

        static void Main(string[] args)
        {
            _config = (new ServiceConfigurationManager()).GetServiceConfiguration();
            var serviceName = "NBNAutoImportService";

            //configure logging
            Log4NetConfiguration.Configure(_config, serviceName);

            HostFactory.Run(x =>
                {
                    x.UseLog4Net();
                    x.Service<PollingService>(s =>
                        {
                            s.ConstructUsing(name => new PollingService());
                            s.WhenStarted(ps => ps.Start());
                            s.WhenStopped(ps => ps.Stop());
                        });
                    x.RunAsLocalSystem();

                    x.SetDescription(
                        "Periodically checks for new NBN import files and runs the importer to ingest them.");
                    x.SetDisplayName("NBN Automatic import service");
                    x.SetServiceName(serviceName);

                    x.BeforeInstall(EnsureConfiguration);
                });

        }

        private static void EnsureConfiguration(InstallHostSettings installHostSettings)
        {
            var fsm = new FileSystemManger();

            fsm.EnsureFolder(_config.NewOrReplaceFolder);
            fsm.EnsureFolder(_config.AppendsFolder);
            fsm.EnsureFolder(_config.ImporterLogFolder);
            fsm.EnsureFolder(_config.ResultFolder);
            fsm.EnsureFolder(_config.TempFolder);
        }
    }
}
