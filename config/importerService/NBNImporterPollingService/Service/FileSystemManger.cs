using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace uk.org.nbn.nbnv.ImporterPollingService.Service
{
    public class FileSystemManger
    {
        public List<FileInfo> GetListOfFiles(string inputFolder, string pattern = "*")
        {
            var di = new DirectoryInfo(inputFolder);

            var q = from fi in di.EnumerateFiles(pattern)
                    orderby fi.CreationTime 
                    select fi;

            return q.ToList();
        }

        public void ClearFilesInFolder(string logFolder)
        {
            var di = new DirectoryInfo(logFolder);

            foreach (var fi in di.EnumerateFiles())
            {
                fi.Delete();
            }
        }

        public string CreateSubFolderForResults(string targetFolder, FileInfo file)
        {
            var di = new DirectoryInfo(targetFolder);

            var newFolderName = Path.GetFileNameWithoutExtension(file.FullName) + "-" + GetCurrentTimestamp();

            var resultDi = di.CreateSubdirectory(newFolderName);
            return resultDi.FullName;
        }

        public void MoveFileToLocation(FileInfo file, string targetFolder)
        {
            var newLocation = targetFolder.EnsureEndsWith(@"\");
            newLocation = newLocation + Path.GetFileName(file.Name);
            file.MoveTo(newLocation);
        }

        public void MoveFilesToFolder(string sourceFolder, string targetFolder, string pattern)
        {
            foreach (var fi in GetListOfFiles(sourceFolder, pattern))
            {
                MoveFileToLocation(fi, targetFolder);
            }
        }

        public void EnsureFolder(string pathToEnsure)
        {
            Directory.CreateDirectory(pathToEnsure);
        }

        public void SaveStreamToFile(StreamReader source, string targetFolder, string targetFile)
        {
            var targetPath = targetFolder.EnsureEndsWith(@"\") + targetFile;

            using (var file = File.CreateText(targetPath))
            {
                char[] buffer = new char[8192];
                int length;
                while ((length = source.Read(buffer, 0, buffer.Length)) > 0)
                {
                    file.Write(buffer, 0, length);
                }
            }
        }

        private string GetCurrentTimestamp()
        {
            return DateTime.Now.ToString("yyyyMMddHHmmssffff");
        }
    }
}
