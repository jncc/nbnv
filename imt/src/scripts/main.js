require.config({
	stubModules: ['cs', 'hbs'],
	shim: {
        'handlebars': { exports: 'Handlebars' },
		'openlayers': { exports: 'OpenLayers' },
		'jquery-ui': {
            exports: '$',
            deps: ['jquery']
        }
    },
	paths: {
		'hbs' : '../vendor/requirejs-hbs/hbs',
		'handlebars' : '../vendor/handlebars/Handlebars',
		'text' : '../vendor/requirejs-text/text',
		'jquery' : '../vendor/jquery/jquery',
		'jquery-ui': '../vendor/jquery-ui/ui/jquery-ui',
		'underscore': '../vendor/underscore-amd/underscore',
		'backbone': '../vendor/backbone-amd/backbone',
		'openlayers': '../vendor/openlayers-js/index'
	}
});

require(['models/App', 'views/AppView'], function(App, AppView) {
	new AppView({model : new App()});
});