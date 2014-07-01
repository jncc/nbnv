var importerModule = angular.module('importer', ['ngRoute', 'ngAnimate'])
    .config(['$routeProvider', '$locationProvider',
        function($routeProvider, $locationProvider) {
            $routeProvider
                .when('/View/:name', {
                    templateUrl: 'View.html',
                    controller: 'ViewController',
                    controllerAs: 'view'
                });

            // configure html5 to get links working on jsfiddle
            $locationProvider.html5Mode(true);
        }])
    .controller('ViewController', ['$routeParams', function($routeParams) {
        this.name = "ViewController";
        this.params = $routeParams;
    }]);

// configure the module.
// in this example we will create a greeting filter
importerModule.filter('greet', function() {
    return function(name) {
        return 'Hello, ' + name + '!';
    };
});