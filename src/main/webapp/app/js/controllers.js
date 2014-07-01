'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
    .controller('SearchCtrl', ['$scope', '$http', '$auth', function($scope, $http, $auth) {
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
                        $scope.results.push(xebian.email);
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

            $http.get('api/xebian?email='+$scope.email).then(function(response) {
                $scope.skills = response.data.skills;
            });
        });

        $scope.skills = [];
        $scope.newSkill = "";
        $scope.addSkill = function() {
            if (_.indexOf($scope.skills, $scope.newSkill) === -1) {
                $http({
                    method : 'GET',
                    url : 'api/xebian',
                    params : {
                        "email": $scope.email
                    }
                }).then(function(response) {
                    $scope.results = [];
                    $scope.id=_.first(response.data).id.value;

                    $http.put('api/xebian/'+$scope.id, $scope.newSkill).then(function(response) {
                        $scope.skills = response.data.skills;
                        $scope.newSkill = "";
                    });
                });
            }
        }
    }]);
