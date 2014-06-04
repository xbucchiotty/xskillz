'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
    .controller('SearchCtrl', ['$scope', '$http', '$auth', function($scope, $http, $auth) {
        $scope.search = function() {
            $http({
                method : 'POST',
                url : 'api/search',
                data : {
                    search: $scope.query
                }
            });
        }
    }])
    .controller('ProfileCtrl', ['$scope', '$http', function($scope, $http) {
        var email = "";
        var promise = $auth.getAuth();
        promise.then(function (response) {
            email = response.data;
        });

        $scope.tags = ["Angular", "Java"];

        $scope.addTag = function() {
        }
    }]);
