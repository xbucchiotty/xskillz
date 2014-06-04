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
        });

        $scope.skills = ["Angular", "Java", "dsfds"];

        $scope.newSkill = "";
        $scope.addSkill = function() {
            if (_.indexOf($scope.skills, $scope.newSkill) === -1) {
                $scope.skills.push($scope.newSkill);
            }
        }
    }]);
