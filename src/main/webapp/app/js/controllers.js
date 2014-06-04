'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
    .controller('SearchCtrl', ['$scope', '$http', function($scope, $http) {
          $scope.query = "Angular";

        $scope.search = function() {
            $http({
                method : 'POST',
                url : '/api/search',
                data : {
                    search: $scope.query
                }
            });
        }
    }])
    .controller('ProfileCtrl', ['$scope', function($scope) {

    }]);
