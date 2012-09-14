# SQL Server

The sqljdbc4.jar can be retrieved from 
http://www.microsoft.com/en-gb/download/details.aspx?id=11774

Add it to your local Maven repository with
mvn install:install-file -Dfile=sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar

# Map Server

The mapscript.jar can be retrieved from
http://www.gisinternals.com/sdk/PackageList.aspx?file=release-1600-x64-gdal-1-9-1-mapserver-6-0-3.zip

Add it to your local Maven repository with
mvn install:install-file -Dfile=mapscript.jar -DgroupId=EDU.umn.gis -DartifactId=mapscript -Dversion=3.0.6 -Dpackaging=jar

#JCoord
The jcoord-1.1-b.jar file can be retrieved from
http://www.jstott.me.uk/jcoord/jcoord-1.1-b.jar

Add it to your local Maven repository with
mvn install:install-file -Dfile=jcoord-1.1-b.jar -DgroupId=uk.me.jstott -DartifactId=jcoord -Dversion=1.1-b -Dpackaging=jar

