module.exports = (grunt)->
  #Load grunt tasks
  grunt.loadNpmTasks 'grunt-combine-harvester'
  grunt.loadNpmTasks 'grunt-contrib-requirejs'
  grunt.loadNpmTasks 'grunt-contrib-jasmine'
  grunt.loadNpmTasks 'grunt-contrib-copy'
  grunt.loadNpmTasks 'grunt-contrib-connect'
  grunt.loadNpmTasks 'grunt-contrib-clean'
  grunt.loadNpmTasks 'grunt-contrib-less'
  grunt.loadNpmTasks 'grunt-contrib-cssmin'
  grunt.loadNpmTasks 'grunt-contrib-watch'
  grunt.loadNpmTasks 'grunt-contrib-compress'
  grunt.loadNpmTasks 'grunt-contrib-coffee'

  #Configure tasks
  grunt.initConfig
    jasmine :
      test:
        options :
          specs : 'test-compiled/**/*spec.js'
          template: require 'grunt-template-jasmine-requirejs'
          templateOptions: 
            requireConfigFile: 'src/scripts/main.js'
            requireConfig: baseUrl: 'src/scripts' 
          junit : path : 'junit' 

    coffee: 
      test :
        expand: true
        cwd: 'test'
        src: ['**/*.coffee']
        dest: 'test-compiled'
        ext: '.spec.js'
    
    requirejs: 
      compile: 
        options: 
          exclude:['coffee-script']
          baseUrl: 'dist/scripts'
          out: 'dist/scripts/main.js'
          name: 'main'
          mainConfigFile: 'dist/scripts/main.js'

    less: 
      development:
        files: 'src/css/app.css' : 'src/less/app.less'
    
    cssmin: 
      build:
        src: 'dist/css/app.css'
        dest: 'dist/css/app.css'
    
    combine_harvester: 
      openlayers:
        options:
          root: 'src/vendor/openlayers/lib/'
        files:
          'src/vendor/OpenLayers-custom.js': [
            'src/vendor/openlayers/lib/OpenLayers/Map.js'
            'src/vendor/openlayers/lib/OpenLayers/Kinetic.js'
            'src/vendor/openlayers/lib/OpenLayers/Projection.js'
            'src/vendor/openlayers/lib/OpenLayers/Layer/Bing.js'
            'src/vendor/openlayers/lib/OpenLayers/Layer/WMS.js'
            'src/vendor/openlayers/lib/OpenLayers/Control/TouchNavigation.js'
            'src/vendor/openlayers/lib/OpenLayers/Control/Navigation.js'
            'src/vendor/openlayers/lib/OpenLayers/Control/Zoom.js'
            'src/vendor/openlayers/lib/OpenLayers/Control/Attribution.js'
            'src/vendor/openlayers/lib/OpenLayers/Control/DrawFeature.js'
            'src/vendor/openlayers/lib/OpenLayers/Handler/RegularPolygon.js'
            'src/vendor/openlayers/lib/OpenLayers/Layer/Vector.js'
            'src/vendor/openlayers/lib/OpenLayers/Renderer/SVG.js'
            'src/vendor/openlayers/lib/OpenLayers/Renderer/Canvas.js'
            'src/vendor/openlayers/lib/OpenLayers/Protocol/HTTP.js'
            'src/vendor/openlayers/lib/OpenLayers/Strategy/Fixed.js'
            'src/vendor/openlayers/lib/OpenLayers/TileManager.js'
            'src/vendor/openlayers/lib/OpenLayers/Format/WKT.js'
          ]

    watch: 
      files: "src/less/*"
      tasks: ["less"]

    clean: 
      test:['test-js', 'test-compiled']
      prep:['dist', 'src/css']

    copy: 
      build:
        files: [ expand: true, cwd: 'src/', src: ['**'], dest : 'dist' ]

    compress:
      package:
        options:
          archive: 'dist/imt.war'
          mode: 'zip'
        expand: true
        cwd: 'dist/'
        src: ['**/*']

    connect: 
      development: 
        options: port: 8080, base: 'src'

  grunt.registerTask 'bower-install', ->
    require('bower').commands.install().on('end', do @async)

  grunt.registerTask 'prep', ['clean', 'bower-install', 'combine_harvester:openlayers']
  grunt.registerTask 'test', ['clean:test', 'coffee', 'jasmine']
  grunt.registerTask 'develop', ['connect', 'less', 'watch']
  grunt.registerTask 'build', ['less', 'test', 'copy', 'cssmin', 'requirejs']
  grunt.registerTask 'package', ['build', 'compress']
  grunt.registerTask 'default', ['prep', 'package'] #register the default task as build