module.exports = function (grunt) {
	//Load grunt tasks
	grunt.loadNpmTasks('grunt-contrib-requirejs');
	grunt.loadNpmTasks('grunt-contrib-jasmine');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-connect');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-less');
	grunt.loadNpmTasks('grunt-contrib-watch');
	
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
					baseUrl: 'src/scripts',
					out: 'dist/scripts/main.js',
					name: 'main',
					mainConfigFile: 'src/scripts/main.js'
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
				files: [
					{ src: 'src/index.html', dest : 'dist/index.html' },
					{ src: 'src/css/app.css', dest : 'dist/css/app.css' },
					{ src: 	['jquery-ui/themes/black-tie/**', 
							 'requirejs/require.js'
							], dest : 'dist/vendor/', cwd: 'src/vendor/'}
				]
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
	grunt.registerTask('test', ['prep', 'jasmine']);
	grunt.registerTask('develop', ['connect', 'less', 'watch']);
	grunt.registerTask('build', ['less', 'test', 'requirejs' , 'copy']);
	grunt.registerTask('default', ['build']); //register the default task as build
};