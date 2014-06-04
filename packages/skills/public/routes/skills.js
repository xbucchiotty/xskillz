'use strict';

angular.module('mean.skills').config(['$stateProvider',
    function($stateProvider) {
        $stateProvider.state('skills example page', {
            url: '/skills/example',
            templateUrl: 'skills/views/index.html'
        });
    }
]);
