require.config({
	stubModules: ['cs', 'hbs'],
	shim: {
        'handlebars': { exports: 'Handlebars' },
		'openlayers': { exports: 'OpenLayers', deps: ['proj4js']},
		'jquery-ui': { exports: '$', deps: ['jquery']}
    },
	paths: {
		'cs' : '../vendor/require-cs/cs',
		'coffee-script': '../vendor/coffee-script/extras/coffee-script',
		'hbs' : '../vendor/requirejs-hbs/hbs',
		'handlebars' : '../vendor/handlebars/Handlebars',
		'text' : '../vendor/requirejs-text/text',
		'jquery' : '../vendor/jquery/jquery',
		'jquery-ui': '../vendor/jquery-ui/ui/jquery-ui',
		'underscore': '../vendor/underscore-amd/underscore',
		'backbone': '../vendor/backbone-amd/backbone',
		'openlayers': '../vendor/openlayers-js/index',
		'proj4js' : '../vendor/proj4js/lib/proj4js-combined'
	}
});

require(['cs!models/App', 'cs!views/AppView'], function(App, AppView) {
	new AppView({model : out = new App()});
});