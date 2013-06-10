module.exports = function (grunt) {
	//Load grunt tasks
	grunt.loadNpmTasks('grunt-contrib-requirejs');
	grunt.loadNpmTasks('grunt-contrib-jasmine');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-connect');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-less');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-compress');
	
	//Configure tasks
	grunt.initConfig({
		jasmine : {
			compile: {
				options : {
					specs : 'test/**/*spec.js',
					template: require('grunt-template-jasmine-requirejs'),
					templateOptions: {
						requireConfigFile: 'src/scripts/main.js',
						requireConfig: { baseUrl: 'src/scripts' }
					},
					junit : { path : 'junit' }
				}
			}
		},
		requirejs: {
			compile: {
				options: {
					exclude:['coffee-script'],
					baseUrl: 'dist/scripts',
					out: 'dist/scripts/main.js',
					name: 'main',
					mainConfigFile: 'dist/scripts/main.js'
				}
			}
		},
		less: {
			development: {
				files: { 'src/css/app.css' : 'src/less/app.less'}
			}
		},
		watch: {
            files: "src/less/*",
            tasks: ["less"]
        },
		clean: {
			prep:['dist', 'src/css']
		},
		copy: {
			build: {
				files: [{ expand: true, cwd: 'src/', src: ['**'], dest : 'dist'}]
			}
		},
		compress: {
			package: {
				options: {
					archive: 'dist/imt.war',
					mode: 'zip'
				},
				expand: true,
				cwd: 'dist/',
				src: ['**/*']
			}
		},
		connect: {
			development: {
				options: { port: 8080, base: 'src' }
			}
		}
	});

	//Define grunt tasks	
	grunt.registerTask('bower-install', function() {
		require('bower').commands.install().on('end', this.async());
	});

	grunt.registerTask('prep', ['clean', 'bower-install']);
	grunt.registerTask('test', ['jasmine']);
	grunt.registerTask('develop', ['connect', 'less', 'watch']);
	grunt.registerTask('build', ['less', 'test', 'copy', 'requirejs']);
	grunt.registerTask('package', ['build', 'compress']);
	grunt.registerTask('default', ['prep', 'package']); //register the default task as build
};