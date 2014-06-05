'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
    .controller('SearchCtrl', ['$scope', '$http', '$auth', function($scope, $http, $auth) {
        $http.get('api/xebian').then(function(response) {
            $scope.xebians = response.data;
        });

        $scope.search = function() {
            $http({
                method : 'GET',
                url : 'api/xebian',
                params : {
                    "q": $scope.query
                }
            }).then(function(response) {
                    $scope.results = [];
                    _.forEach(response.data, function(xebian) {
                        $scope.results.push(xebian.id.email);
                    });
                });
            /*
            $scope.results = [];
            _.forEach($scope.xebians, function(xebian) {
                if (_.contains(xebian.skills, {"name": $scope.query})){
                    $scope.results.push(xebian.id);
                }
            });
            */

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
                    $scope.newSkill = "";
                });
            }
        }
    }]);
