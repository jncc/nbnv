namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public class ServiceConfiguration
    {
        public string ImporterCommandLine { get; set; }

        public string ImporterLogFolder { get; set; }

        public string SourceFolder { get; set; }

        public string ResultFolder { get; set; }

        public int PollingIntervalInMilliseconds { get; set; }

        public string TempFolder { get; set; }

        public string JavaExePath { get; set; }

        public LogLevel LogThreshold { get; set; }
    }
}
