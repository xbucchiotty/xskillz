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
    .controller('ProfileCtrl', ['$scope', '$http', '$auth', function($scope, $http, $auth) {
        var email = "";
        var promise = $auth.getAuth();
        promise.then(function (response) {
            email = response.data;

            $http.get('api/xebian/'+email, function(data) {
                $scope.skills = data;
            });
        });

        $scope.skills = [];

        $scope.newSkill = "";
        $scope.addSkill = function() {
            if (_.indexOf($scope.skills, $scope.newSkill) === -1) {
                $http.put('api/xebian/'+email, $scope.newSkill, function() {
                });

                $http.get('api/xebian/'+email, function(data) {
                    $scope.skills = data;
                });
            }
        }
    }]);
