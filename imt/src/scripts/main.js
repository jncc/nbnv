require.config({
  stubModules: ['cs', 'tpl'],
  shim: {
    'openlayers': { exports: 'OpenLayers', deps: ['proj4js']},
    'jquery.ui.core': {deps: ['jquery']},
    'jquery.ui.mouse': {deps: ['jquery.ui.widget']},
    'jquery.ui.sortable': {deps: ['jquery.ui.core', 'jquery.ui.mouse', 'jquery.ui.widget', 'jquery.ui.touch-punch']},
    'jquery.ui.draggable': {deps: ['jquery.ui.core', 'jquery.ui.mouse', 'jquery.ui.widget', 'jquery.ui.touch-punch']},
    'jquery.ui.resizable': {deps: ['jquery.ui.core', 'jquery.ui.mouse', 'jquery.ui.widget', 'jquery.ui.touch-punch']},
    'jquery.ui.slider': {deps: ['jquery.ui.core', 'jquery.ui.mouse', 'jquery.ui.widget', 'jquery.ui.touch-punch']},
    'jquery.ui.accordion': {deps: ['jquery.ui.core', 'jquery.ui.widget']},
    'jquery.ui.tabs': {deps: ['jquery.ui.core', 'jquery.ui.widget']},
    'jquery.ui.button': {deps: ['jquery.ui.core', 'jquery.ui.widget']},
    'jquery.ui.effect-slide': {deps: ['jquery.ui.effect']},
    'jquery.ui.dialog': {deps: ['jquery.ui.core', 'jquery.ui.widget', 'jquery.ui.button', 'jquery.ui.draggable', 'jquery.ui.mouse', 'jquery.ui.position', 'jquery.ui.resizable', 'jquery.ui.touch-punch']},
    'jquery-md5': { exports: '$', deps: ['jquery']},
    'DataTables' : { exports: '$', deps: ['jquery']}
  },
  paths: {
    'cs' : '../vendor/require-cs/cs',
    'coffee-script': '../vendor/coffee-script/extras/coffee-script',
    'tpl' : '../vendor/requirejs-tpl/tpl',
    'text' : '../vendor/requirejs-text/text',
    'DataTables': '../vendor/datatables/media/js/jquery.dataTables',
    'jquery' : '../vendor/jquery/jquery',
    'jquery.ui.touch-punch' : '../vendor/jquery-ui-touch-punch/jquery.ui.touch-punch',
    'jquery.ui.core': '../vendor/jquery-ui/ui/jquery.ui.core',
    'jquery.ui.mouse': '../vendor/jquery-ui/ui/jquery.ui.mouse',
    'jquery.ui.widget': '../vendor/jquery-ui/ui/jquery.ui.widget',
    'jquery.ui.sortable': '../vendor/jquery-ui/ui/jquery.ui.sortable',
    'jquery.ui.accordion': '../vendor/jquery-ui/ui/jquery.ui.accordion',
    'jquery.ui.dialog': '../vendor/jquery-ui/ui/jquery.ui.dialog',
    'jquery.ui.button': '../vendor/jquery-ui/ui/jquery.ui.button',
    'jquery.ui.draggable': '../vendor/jquery-ui/ui/jquery.ui.draggable',
    'jquery.ui.position': '../vendor/jquery-ui/ui/jquery.ui.position',
    'jquery.ui.resizable': '../vendor/jquery-ui/ui/jquery.ui.resizable',
    'jquery.ui.slider': '../vendor/jquery-ui/ui/jquery.ui.slider',
    'jquery.ui.tabs': '../vendor/jquery-ui/ui/jquery.ui.tabs',
    'jquery.ui.effect': '../vendor/jquery-ui/ui/jquery.ui.effect',
    'jquery.ui.effect-slide': '../vendor/jquery-ui/ui/jquery.ui.effect-slide',
    'jquery-md5': '../vendor/jquery-md5/jquery.md5',
    'underscore': '../vendor/underscore-amd/underscore',
    'backbone': '../vendor/backbone-amd/backbone',
    'openlayers': '../vendor/openlayers-js/index',
    'proj4js' : '../vendor/proj4js/lib/proj4js-combined',
    'select2' : '../vendor/select2/select2'
  },
  waitSeconds:45
});

require(['jquery', 'backbone', 'cs!models/App', 'cs!views/AppView', 'cs!routers/StateRouter', 'cs!routers/GetURLRouter', 'cs!helpers/GoogleAnalytics'], function($, Backbone, App, AppView, Router, GetURLRouter, GA) {
  app = new App();
  getMapRouter = new GetURLRouter({model: app});
  getMapRouter.navigate(location.search);
  view = new AppView({model : app});
  router = new Router({model: app});
  Backbone.history.start();
  router.refresh();
  GA.listen(app, view);

  $('#imt').removeClass('loading'); //remove the loading class
});