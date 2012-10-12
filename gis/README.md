# Running the GIS Application

The GIS application utilises MapServers CGI application in order
to create customised GIS maps which dynamically change. A 
result of this is that the web container must be able to access
the MapServer CGI application on the localhost. This is because
the GIS application dynamically creates map files which are 
referenced by path by the CGI application

Below documents how to install MapServer along side a tomcat 
instance to create a web container which this
appliation can be deployed to.

# Obtaining a MapServer Distribution

First obtain a binary distribution of MapServer the word size is
independent from your JVM as the application runs in a seperate
process. For a out of the box configured MapServer, we recommend
[MapServer for Windows - MS4W](http://www.maptools.org/ms4w/)

# Installing MS4W to be used by the GIS application
1. Install the distribution remembering to register the appache 
installation to a port restricted to the localhost (i.e. Not port 80)

2. Configure your server specific GIS properties file to point to 
the installation. The default value is configured in gis.properties.
(See the Properties project on how to create a server specific version)

3. Install FastCGI (Recommended)
The following documents how to install [FastCGI](http://www.maptools.org/ms4w/index.phtml?page=README_INSTALL.html#f-fastcgi)

At the time of writting, points 5, 7 (Default config of the gis 
application assumes fcgi) and 8 are not applicable for the installation 
of mapserver.

4. If you which to use any plugins, ensure that these are on the 
PATH System Enviroment Variable. At the time of writing this 
application uses the msplugin_mssql2008.dll. To use this add 
$MAPSERVER$\Apache\specialplugins to the PATH variable