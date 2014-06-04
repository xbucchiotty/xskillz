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
        $scope.email = "";
        var promise = $auth.getAuth();
        promise.then(function (response) {
            $scope.email = response.data;

            $http.get('api/xebian/'+$scope.email).then(function(response) {
                $scope.skills = response.data.skills;
            });
        });

        $scope.skills = [];
        $scope.newSkill = "";
        $scope.addSkill = function() {
            if (_.indexOf($scope.skills, $scope.newSkill) === -1) {
                $http.put('api/xebian/'+$scope.email, $scope.newSkill).then(function(response) {
                    $scope.skills = response.data.skills;
                });
            }
        }
    }]);
