

SQL Server

The sqljdbc4.jar can be retrieved from 
http://www.microsoft.com/en-gb/download/details.aspx?id=11774

Add it to your local Maven repository with
mvn install:install-file -Dfile=sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar

Map Server

The mapscript.jar can be retrieved from
TODO: chris to choose a distribution

Add it to your local Maven repository with
mvn install:install-file -Dfile=mapscript.jar -DgroupId=EDU.umn.gis -DartifactId=mapscript -Dversion=3.0.6 -Dpackaging=jar