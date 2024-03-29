# NBN Gateway Code Base

This is the code base for the bulk of the NBN Gateway. The code is built at CEH by the [bamboo continuous integration server](http://bamboo.ceh.ac.uk).

Puppet is used to manage the NBN Gateway web servers. The [nbngateway puppet module](http://github.com/nerc-ceh/puppet-nbngateway.git) can be modified to suite the requirements of the nbn gateway web servers.

Some components have been made open source, such as the imt:

- [Interactive Map Tool](https://github.com/nerc-ceh/nbn-imt.git)

If you would like to see the nbn gateway in action create your own webserver with our [vagrant project](https://github.com/nerc-ceh/nbngateway-vagrant.git). Or alternatively visit:
 
- Production https://data.nbn.org.uk
- Staging https://staging-data.nbn.org.uk


## Maven Dependencies

Some of the maven dependencies required by the nbn gateway have not been made open source. You will either need to add a repository which does contain these in your **settings.xml** or manually install them as followed.

### SQL Server

The sqljdbc4.jar can be retrieved from 
http://www.microsoft.com/en-gb/download/details.aspx?id=11774

Add it to your local Maven repository with

    mvn install:install-file -Dfile=sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar

### JCoord
The jcoord-1.1-b.jar file can be retrieved from

    http://www.jstott.me.uk/jcoord/jcoord-1.1-b.jar

Add it to your local Maven repository with

    mvn install:install-file -Dfile=jcoord-1.1-b.jar -DgroupId=uk.me.jstott -DartifactId=jcoord -Dversion=1.1-b -Dpackaging=jar