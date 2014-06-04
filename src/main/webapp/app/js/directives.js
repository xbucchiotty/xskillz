'use strict';

/* Directives */


angular.module('myApp.directives', [])
    .directive('appVersion', ['version', function(version) {
        return function(scope, elm, attrs) {
          elm.text(version);
        };
    }])
    .directive('who', ['$auth', function($auth) {
        return function(scope, elm) {
            var promise = $auth.getAuth();
            promise.then(function (response) {
                elm.text(response.data);
            });
        };
    }]);
