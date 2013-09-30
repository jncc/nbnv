using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using log4net;
using log4net.Appender;
using log4net.Core;
using log4net.Layout;
using log4net.Repository.Hierarchy;

namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public enum LogLevel
    {
        Debug,
        Info,
        Warn,
        Error
    }


    public static class Log4NetConfiguration
    {
        public static void Configure(ServiceConfiguration configuration, string serviceName)
        {
            var hierarchy = (Hierarchy) LogManager.GetRepository();
            hierarchy.Root.RemoveAllAppenders();

            // see http://logging.apache.org/log4net/release/sdk/log4net.Layout.PatternLayout.html

            var layout = new PatternLayout
                {
                    ConversionPattern = "%date{dd-MMM-yyyy HH:mm:ss}  %-5level  %message%n%exception%n"
                };

            Level level;
            switch (configuration.LogThreshold)
            {
                case LogLevel.Info:
                    level = Level.Info;
                    break;
                case LogLevel.Debug:
                    level = Level.Debug;
                    break;
                case LogLevel.Warn:
                    level = Level.Warn;
                    break;
                case LogLevel.Error:
                    level = Level.Error;
                    break;
                default:
                    throw new Exception("Unhandled log level");
            }

            //var fileAppender = new RollingFileAppender
            //{
            //    Layout = layout,
            //    AppendToFile = true,
            //    RollingStyle = RollingFileAppender.RollingMode.Composite,
            //    DatePattern = "yyyy-MM-dd",
            //    File = Path.Combine(configuration.ImporterLogFolder ?? String.Empty, "NBNAutoImportlog"),
            //    LockingModel = new FileAppender.MinimalLock(),
            //    MaxFileSize = 2097152, //2Mb
            //    CountDirection = 1,
            //    StaticLogFileName = false,
            //    //File = settings.LogDirectory
            //    Threshold = level
            //};

            var eventLogAppender = new EventLogAppender()
                {
                    ApplicationName = serviceName,
                    Layout = layout,
                    Threshold = level
                };

            var consoleAppender = new ConsoleAppender
                {
                    Layout = layout,
                };

            layout.ActivateOptions();

            eventLogAppender.ActivateOptions();
            log4net.Config.BasicConfigurator.Configure(eventLogAppender);

            //fileAppender.ActivateOptions();
            //log4net.Config.BasicConfigurator.Configure(fileAppender);

            consoleAppender.ActivateOptions();
            log4net.Config.BasicConfigurator.Configure(consoleAppender);
        }
    }
}
