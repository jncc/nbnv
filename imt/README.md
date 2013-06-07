# NBN Interactive Map Tool

Welcome to the code base for the NBN Gateway Interactive Map Tool (IMT). The following readme explains the structure and technologies used in this project. For further documentation for this product view the [CEH wiki](https://wiki.ceh.ac.uk/display/nbn/Interactive+Map+Tool).

## Prerequisites for building the project
The IMT is a built using [grunt](http://gruntjs.com) and therefore requires [node.js](http://nodejs.org). To build and test the project, you will first need to set up your environment:

1. Install node.js
2. Install grunt 
 
        npm install -g grunt-cli
		
### Gotchas
By default bower will try to pull git repositories over port 9418, default for *git://* scheme. If you are behind a firewall which blocks this port then you may want to :

        git config --global url."https://".insteadOf git://
	
Once this has been done, grab the git repository and then :

1. Obtain the node packages required for building (these are defined in package.json)

        npm install

2. Execute the grunt task you require. e.g.

        grunt build

## Technologies
### Building and Dependency Management
The following are used for building and dependency management

* [node.js](http://nodejs.org) - Used for obtain the packages used in grunt for building the IMT
* [grunt](http://gruntjs.com) - Used for performing building, testing and other development tasks such as creating a simple dev server.
* [bower](http://bower.io) - Used for obtaining client side dependencies such as jQuery and bootstrap.
* [LESS](http://lesscss.org) - Used for compiling style sheets

### Testing
The client side code is tested using [jasmine](http://pivotal.github.io/jasmine) tests. These can be executed at the command line using the grunt task *test*. Testing is performed in the headless webkit engine [PhantomJS](http://phantomjs.org/)

### Client side libraries

* [RequireJS](http://requirejs.org) - The AMD loader which allows us to structure the IMT in separate JavaScript files. By using this, each script file which we write defines which other modules of the application the script requires. Using the r.js optimizer, we can output a single script file.

* [Backbone.js](http://backbonejs.org/) - The MV* engine which powers the event model and structures the application into models/views/collections and controllers.

* [Handlebars](http://handlebarsjs.com) - The templating engine for creating simple templates. With the require plugin for handlebars loaded, precompiled templates can be depended on. During the r.js optimization step, Handlebars templates will be precompiled by the handlebars plugin.

* [jQuery](http://jquery.com) - for manipulating the dom

* [OpenLayers](http://openlayers.org/) - the mapping engine

## Grunt tasks
The following defines the grunt tasks available and when you should use them.

* prep - Cleans the dist folder and performs a bower install, this will obtain all the client side modules which the IMT relies. After this you will be able to start up a server hosting the src folder and update code. Changes to any require.js code will be reflected by refreshing your browser window.

* jasmine - Executes all of the specs in the test folder against the source code in src. Relies on prep being called first.

* develop - Loads up a simple static servicing server hosting the src folder on port 8080 of your localhost. Perform this after prep to begin development with instant updates to code reflected in the browser. Also performs a watch on the less css folder and performs LESS compilation when a .less file has been modified.

* test - performs prep and jasmine

* build - Populates the dist folder with all the code optimized ready for deployment.

