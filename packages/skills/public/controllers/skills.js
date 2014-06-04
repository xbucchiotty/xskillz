'use strict';

angular.module('mean.skills').controller('SkillsController', ['$scope', 'Global', 'Skills',
    function($scope, Global, Skills) {
        $scope.global = Global;
        $scope.package = {
            name: 'skills'
        };
    }
]);
