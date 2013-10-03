namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public static class StringExtensions
    {
        public static string EnsureEndsWith(this string s, string text)
        {
            if (s != null && s.EndsWith(text)) return s;

            return s + text;
        }
    }
}
