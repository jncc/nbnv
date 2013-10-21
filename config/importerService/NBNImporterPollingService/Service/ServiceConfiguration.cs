namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public class ServiceConfiguration
    {
        public string ImporterCommandLine { get; set; }

        public string ImporterLogFolder { get; set; }

        public string NewOrReplaceFolder { get; set; }

        public string ResultFolder { get; set; }

        public int PollingIntervalInMilliseconds { get; set; }

        public string TempFolder { get; set; }

        public string JavaExePath { get; set; }

        public LogLevel LogThreshold { get; set; }

        public string AppendsFolder { get; set; }
    }
}
