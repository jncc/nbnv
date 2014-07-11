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

    importerModule.controller('MetadataController', function ($scope, $http) {
        $scope.allOrganisations = {};
        $scope.allOrganisations = [{name:"test"}, {name:"alpha"}, {name:"beta"}];

        $scope.resetMetadata = function(){
            $scope.metadata = {};
            $scope.metadata.title = "";
            $scope.metadata.organisation = "";
            $scope.metadata.description = "";
            $scope.metadata.methodsOfDataCapture = "";
            $scope.metadata.purposeOfDataCapture = "";
            $scope.metadata.geographicalCoverage = "";
            $scope.metadata.temporalCoverage = "";
            $scope.metadata.dataQuality = "";
            $scope.metadata.additionalInfo = "";
            $scope.metadata.useConstraints = "";
            $scope.metadata.accessConstraints = "";
            $scope.metadata.geographicResolution = "";
            $scope.metadata.adminDetails = {};
            $scope.metadata.adminDetails.name = "";
            $scope.metadata.adminDetails.phone = "";
            $scope.metadata.adminDetails.email = "";
            $scope.metadata.access = {};
            $scope.metadata.resolution = ""; // radio button
            $scope.metadata.recordAttributes = ""; // radio button
            $scope.metadata.recorderNames = ""; // radio button
            $scope.metadata.insertionType = ""; // radio button
        };

        $scope.resetMetadata(); // call it to perform initialization

        $scope.updateGeoResolution = function(key, value){
            $scope.metadata[key] = value;
        }

        $scope.upload = function() {
            console.log(JSON.stringify($scope.metadata));
            $http({
                method : 'POST',
                url : '/rest/upload-metadata',
                headers: {'Content-Type' : 'application/json'},
                data : $scope.metadata
            })
        }

    });

    var resetMetadata
})();
