require.config({
	stubModules: ['cs', 'hbs'],
	shim: {
    'handlebars': { exports: 'Handlebars' },
		'openlayers': { exports: 'OpenLayers', deps: ['proj4js']},
		'jquery-ui': { exports: '$', deps: ['jquery']},
		'jquery-md5': { exports: '$', deps: ['jquery']},
		'DataTables' : { exports: '$', deps: ['jquery']}
  },
	paths: {
		'cs' : '../vendor/require-cs/cs',
		'coffee-script': '../vendor/coffee-script/extras/coffee-script',
		'hbs' : '../vendor/requirejs-hbs/hbs',
		'handlebars' : '../vendor/handlebars/Handlebars',
		'text' : '../vendor/requirejs-text/text',
		'DataTables': '../vendor/datatables/media/js/jquery.dataTables',
		'jquery' : '../vendor/jquery/jquery',
		'jquery-ui': '../vendor/jquery-ui/ui/jquery-ui',
		'jquery-md5': '../vendor/jquery-md5/jquery.md5',
		'jquery-colorpicker': '../vendor/jquery-colorpicker/js/colorpicker',
		'underscore': '../vendor/underscore-amd/underscore',
		'backbone': '../vendor/backbone-amd/backbone',
		'openlayers': '../vendor/openlayers-js/index',
		'proj4js' : '../vendor/proj4js/lib/proj4js-combined',
		'select2' : '../vendor/select2/select2'
	}
});

require(['backbone', 'cs!models/App', 'cs!views/AppView', 'cs!routers/GetURLRouter'], function(Backbone, App, AppView, GetURLRouter) {
	app = new App();
  router = new GetURLRouter({model: app});
	view = new AppView({model : app});
	router.navigate(location.search);
});