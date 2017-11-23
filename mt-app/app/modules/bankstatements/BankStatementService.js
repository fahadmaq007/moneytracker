'use strict';
angular.module('mt-app')
.service('BankStatementService',['$q', '$http','$rootScope', '$upload', function($q,$http,$rootScope, $upload) {
  this.controllerScope = undefined;
  this.domainSearchDto = {
        'page': { 'pageNumber': 0, 'pageSize': 100}
  };
  this.init = function(controllerScope) {    
      this.controllerScope = controllerScope;
      this.controllerScope.domainSearchDto = this.domainSearchDto;
  };
  this.listBankStatements = function(){
      var scope = this.controllerScope;
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/import',
           method: 'GET',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("listBankStatements is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.listExcelColumns = function(){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/import/excelcolumns',
           method: 'GET',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("listExcelColumns is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };

    this.getBankStatementDetails = function(statement){
      var scope = this.controllerScope;
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/import/bankstatementdetails',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: statement
          }).success(function(data, status, headers, config) {
              console.info("getBankStatementDetails is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.importBankStatement = function(statement, file){
      console.info("importBankStatement");
      var fd = new FormData();
      fd.append('file', file);
      var deferred = $q.defer();
      $http.post($rootScope.hosturl+'/mt/api/import/importbankst?' +
       'bankStatementId=' + statement.id +
       '&startRow=' + statement.startRow +
       '&endRow=' + statement.endRow +
       '&dateFormat=' + statement.dateFormat, fd, {
          transformRequest : angular.identity,
          headers : {
            'Content-Type' : undefined,
            'tn': $rootScope.access_token
          }
      }).success(function(data, status, headers, config) {
          console.info("importBankStatement is successful... ");
          deferred.resolve(data);
      }).error(function(data, status) {
          deferred.reject(data);
      });
      var res = deferred.promise;
      return res;
    };
    this.saveBankStatementDetail = function(statement){
      var scope = this.controllerScope;
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/import/storestatement',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: statement
          }).success(function(data, status, headers, config) {
              console.info("saveBankStatementDetail is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.storeImportedTransactions = function(transactions){
      var scope = this.controllerScope;
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/import/storeimportedtransactions',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: transactions
          }).success(function(data, status, headers, config) {
              console.info("storeImportedTransactions is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
}]);

