'use strict';
angular.module('mt-app')
.service('UserService',['$q', '$http','$rootScope',  function($q,$http,$rootScope) {

  this.initialized = false;

  this.searchDto = {
        'page': { 'pageNumber': 0, 'pageSize': 100}
    };

  this.init = function() {
    if (!this.initialized) {
      
      this.initialized = true;
    }
  };
  this.login = function(user){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/user/token',
           method: 'POST',
           headers: {'Content-Type': 'application/json'},
           data: user
          }).success(function(data, status, headers, config) {
              console.info("login is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
  
  this.logout = function(user){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/user/logout',
           method: 'POST',
           headers: {'Content-Type': 'application/json'},
           data: user
          }).success(function(data, status, headers, config) {
              console.info("logout is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };

    this.register = function(user){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/user/register',
           method: 'POST',
           headers: {'Content-Type': 'application/json'},
           data: user
          }).success(function(data, status, headers, config) {
              console.info("register is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
}]);

