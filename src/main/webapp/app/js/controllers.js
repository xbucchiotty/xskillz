'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
    .controller('SearchCtrl', ['$scope', '$http', function($scope, $http) {
          $scope.query = "Angular";

        $scope.search = function() {
            $http.get('/search/' + $scope.query)
                .success(function(data, status, headers, config) {
                    $scope.results = ["Aucun Xebian n'a cette compétence"];
                })
                .error(function(data, status, headers, config) {
                    $scope.results = ["Erreur"];
                });
        }
    }])
    .controller('ProfileCtrl', ['$scope', function($scope) {

    }]);
