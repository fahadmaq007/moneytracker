'use strict';
angular.module('mt-app')
.service('TransactionService',['$q', '$http','$rootScope',  function($q,$http,$rootScope) {
  //$http.defaults.headers.common['x-customtoken'] = 'GMgo64ez1DZRAeVZNUG5kii2kVdkABs3QO7BbVp+vhN6xMA6WeYQAao4dPbYYRQu';
  this.listTransactions = function(dto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/list',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: dto
          }).success(function(data, status, headers, config) {
              console.info("listTransactions is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };

    this.storeTransaction = function(transaction){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/store',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: transaction
          }).success(function(data, status, headers, config) {
              console.info("storeTransaction is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.storeTransactions = function(transactions){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/storetransactions',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: transactions
          }).success(function(data, status, headers, config) {
              console.info("storeTransactions is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
  this.listTopExpenseCategories = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/listtopexpcategories',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("listTopExpenseCategories is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.listTopIncomeCategories = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/listtopinccategories',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("listTopExpenseCategories is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.getExpenseTotalByReport = function(reportBy){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/expensetotalbyreport?reportBy=' + reportBy,
           method: 'GET',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("getExpenseTotalByReport is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.getIncomeTotalByReport = function(reportBy){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/incometotalbyreport?reportBy=' + reportBy,
           method: 'GET',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("getIncomeTotalByReport is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.expenseReport = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/expensereport',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("expenseReport is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.incomeReport = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/incomereport',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("incomeReport is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.getIncomeExpenseTotal = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/incomeexpensetotal',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("getIncomeExpenseTotal is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.listTransactionColumns = function(){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/columns',
           method: 'GET',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("listTransactionColumns is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.getExpenseTotal = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/expensetotal',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("getExpenseTotal is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.deleteTransaction = function(id){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/' + id,
           method: 'DELETE',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("deleteTransaction is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.deleteAllTransaction = function(transactions){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/deleteall',
           method: 'DELETE',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: transactions
          }).success(function(data, status, headers, config) {
              console.info("deleteTransaction is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.expenseReportByCategoryTree = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/expensereportastree',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("expenseReportByCategoryTree is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.listIncExpHistoryReport = function(searchDto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/transactions/incexphistoryreport',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: searchDto
          }).success(function(data, status, headers, config) {
              console.info("listIncExpHistoryReport is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.isValidTransaction = function(t) {
      return t.onDate && t.amount && t.categoryId && t.accountId;
    };
}]);

