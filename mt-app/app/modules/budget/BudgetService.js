'use strict';
angular.module('mt-app')
.service('BudgetService',['$q', '$http','$rootScope',  function($q,$http,$rootScope) {
  this.initialized = false;
  this.newBudget = {
  	'name': ''
  };
  this.newBudgetItem = {
    'budgetId': -1,
    'categoryId': -1,
    'amount': 0,
    'periodCode': 'M',
    'action': {
      'actionIndex': $rootScope.ACTION_NEW
    }
  };
  this.searchDto = {
        'page': { 'pageNumber': 0, 'pageSize': 50},
        'fromDate': new Date("2014-05-01"),
        'toDate': new Date("2014-05-31")
    };

  this.init = function() {
    if (!this.initialized) {
      this.initialized = true;
    }
  };
  this.listBudget = function(dto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/budget/list',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: dto
          }).success(function(data, status, headers, config) {
              console.info("listBudget is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };

    this.save = function(budget){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/budget/store',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: budget
          }).success(function(data, status, headers, config) {
              console.info("save is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.listBudgetItems = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/budget/items',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("listBudgetItems is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.saveBudgetItems = function(budgetItems){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/budget/storeitems',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: budgetItems
          }).success(function(data, status, headers, config) {
              console.info("save is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.budgetedAndSpentReport = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/budget/budgetedvsspentreport',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("budgetedAndSpentReport is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.getTotalBudgeted = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/budget/totalbudgeted',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("totalBudgeted is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
}]);

