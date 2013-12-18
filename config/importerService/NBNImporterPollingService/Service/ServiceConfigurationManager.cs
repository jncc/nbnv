using System;
using System.Configuration;

namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public class ServiceConfigurationManager
    {
        public ServiceConfiguration GetServiceConfiguration()
        {
            

            var config = new ServiceConfiguration()
                {
                    ImporterCommandLine = ConfigurationManager.AppSettings["ImporterCommandLine"],
                    SourceFolder = ConfigurationManager.AppSettings["SourceFolder"],
                    ImporterLogFolder = ConfigurationManager.AppSettings["ImporterLogFolder"],
                    ResultFolder = ConfigurationManager.AppSettings["ResultFolder"],
                    TempFolder = ConfigurationManager.AppSettings["TempFolder"],
                    PollingIntervalInMilliseconds = int.Parse(ConfigurationManager.AppSettings["PollingIntervalInMilliseconds"]),
                    JavaExePath = ConfigurationManager.AppSettings["JavaExePath"],
                    LogThreshold = LogLevel.Debug
                };

            LogLevel logThreshold;
            if (Enum.TryParse(ConfigurationManager.AppSettings["LogThreshold"], out logThreshold))
                config.LogThreshold = logThreshold;

            //do some config checks here
            return config;
        }
    }
}
