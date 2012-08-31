# Running the GIS Application

The GIS application utilises MapServers MapScript in order
to create customised GIS maps which dynamically change. A 
result of this is that the web container that you which to
deploy this application to must be MapScript enabled. 

Below documents how to install MapScript and MapServer along
side a tomcat instance to create a web container which this
appliation can be deployed to.

# Obtaining a MapServer Distribution for your JVM

First obtain a binary distribution of MapServer which matches
the word size which your JVM is running off. At the time of 
writting, the x64 distribution which this application was 
developed against can be found [here](http://www.gisinternals.com/sdk/PackageList.aspx?file=release-1600-x64-gdal-1-9-1-mapserver-6-0-3.zip)

# Installing MapServer to be used by Tomcat

1. Extract the distribution to a path (e.g. C:\MapServer\). We
will refer to this path as $MAPSERVER$ from now on.

2. Add $MAPSERVER$\bin to the PATH System Enviroment variable

3. If you which to use any plugins, ensure that these are also
on the PATH System Enviroment Variable. At the time of writing
this application uses the msplugin_mssql2008.dll. To use this
add $MAPSERVER$\bin\ms\plugins\mssql2008 to the PATH variable

4. Create a new System Enviroment Variable named PROJ_LIB
set its value to $MAPSERVER$\bin\proj\SHARE

5. Copy the mapscript.jar from $MAPSERVER$\bin\ms\java to your
web containers shared lib folder. In the case of tomcat this is
$TOMCAT_HOME$\lib

6. Copy the mapscript.dll from $MAPSERVER$\bin\ms\java to your
web containers shared bin folder. In the case of tomcat this is
$TOMCAT_HOME$\bin

7. Restart tomcat. This application will now run inside your 
tomcat instance.