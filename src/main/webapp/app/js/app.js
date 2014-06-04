'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', [
  'ngRoute',
  'myApp.filters',
  'myApp.services',
  'myApp.directives',
  'myApp.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/skills', {templateUrl: 'partials/skills.html', controller: 'SkillsCtrl'});
  $routeProvider.otherwise({redirectTo: '/skills'});
}]);
