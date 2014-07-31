using System;
using System.IO;
using System.Text.RegularExpressions;


namespace LogParser
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            if (args.Length == 0 || args[0].Length == 0)
            {
                Program.Usage();
            }
            else
            {
                if (!File.Exists(args[0]))
                {
                    Console.Out.WriteLine("File " + args[0] + " does not exist, please provide the name of a valid log file");
                    Program.Usage();
                }
                else
                {
                    var streamReader = new StreamReader(File.OpenRead(args[0]));
                    var streamWriter = new StreamWriter(File.OpenWrite("error-" + args[0]));
                    string line;
                    while ((line = streamReader.ReadLine()) != null)
                    {
                        if (Program.TestLine(line))
                        {
                            streamWriter.WriteLine(Program.ParseLine(line));
                        }
                    }
                    streamReader.Close();
                    streamWriter.Flush();
                    streamWriter.Close();
                }
            }
        }
        private static string ParseLine(string line)
        {
            Match match = Regex.Match(line, @"\[(.+)\].+[A-Z0-9\-]+:(.+)$", RegexOptions.IgnoreCase);
            var recordKey = match.Groups[1].Value;
            var errorMsg = match.Groups[2].Value;
            return recordKey + ',' + errorMsg;
        }
        private static bool TestLine(string line)
        {
            return Regex.IsMatch(line, "ERROR Validation");
        }
        private static void Usage()
        {
            Console.Out.WriteLine("USAGE: LogParser.exe <log file>");
        }
    }
}