'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', [])
    .value('version', '0.1')
    .factory('$auth', function ($http) {
        var email = "test@test.com";
        $http.get("api/auth", {cache: true})
            .success(function (data) {
                email = data;
            });

        return {
            email: email
        }
    });
