(function () {

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
})();
