(function () {
    /*var importerModule = angular.module('importer', ['ngRoute', 'ngAnimate']);
        .config(['$routeProvider', '$locationProvider',
            function ($routeProvider, $locationProvider) {
                $routeProvider
                    .when('/View/:name', {
                        templateUrl: 'View.html',
                        controller: 'ViewController',
                        controllerAs: 'view'
                    });

                // configure html5 to get links working on jsfiddle
                $locationProvider.html5Mode(true);
            }])*/
    var importerModule = angular.module('importer', []);

    importerModule.controller('ViewController', function () {
        this.myval = "working";
        this.selectedView = 1;

        this.selectView = function(selectedView){
            this.selectedView = selectedView;
        }
        this.isSelected = function(currentView){
            return this.selectedView === currentView;
        }
    });
    importerModule.controller('MetaDataController', function () {
        this.metadata = {};
        this.metadata.title = "";
        this.metadata.organisation = "";
        this.metadata.description = "";
        this.metadata.methodsOfDataCapture = "";
        this.metadata.purposeOfDataCapture = "";
        this.metadata.geographicalCoverage = "";
        this.metadata.temporalCoverage = "";
        this.metadata.dataQuality = "";
        this.metadata.additionalInfo = "";
        this.metadata.useConstraints = "";
        this.metadata.accessConstraints = "";
        this.metadata.adminDetails = {};
        this.metadata.adminDetails.name = "";
        this.metadata.adminDetails.phone = "";
        this.metadata.adminDetails.email = "";
        this.metadata.access = {};
        this.metadata.resolution = ""; // radio button
        this.metadata.recordAttributes = ""; // radio button
        this.metadata.recorderNames = ""; // radio button
        this.metadata.insertionType = ""; // radio button




        this.selectView = function(selectedView){
            this.selectedView = selectedView;
        }
        this.isSelected = function(currentView){
            return this.selectedView === currentView;
        }
    });

// configure the module.
// in this example we will create a greeting filter
    importerModule.filter('greet', function () {
        return function (name) {
            return 'Hello, ' + name + '!';
        };
    });
})();
