'use strict';
angular.module('mt-app')
.service('DomainService',['$q', '$http','$rootScope',  function($q,$http,$rootScope) {

  this.searchDto = {
        'page': { 'pageNumber': 0, 'pageSize': 100}
  };

  this.listCategoryTree = function(dto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/categorytree',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: dto
          }).success(function(data, status, headers, config) {
              console.info("listCategoryTree is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.listCategories = function(dto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/categories',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: dto
          }).success(function(data, status, headers, config) {
              console.info("listCategories is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.listAccounts = function(dto){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/accounts',
           method: 'POST',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: dto
          }).success(function(data, status, headers, config) {
              console.info("listAccounts is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.storeCategories = function(categories){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/categories',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: categories
          }).success(function(data, status, headers, config) {
              console.info("storeCategories is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.storeCategoryTree = function(categories){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/categorytree',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: categories
          }).success(function(data, status, headers, config) {
              console.info("storeCategoryTree is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.storeAccounts = function(accounts){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/accounts',
           method: 'PUT',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token},
           data: accounts
          }).success(function(data, status, headers, config) {
              console.info("storeAccounts is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.deleteAccount = function(id){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/account/' + id,
           method: 'DELETE',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("deleteAccount is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.deleteCategory = function(id){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/category/' + id,
           method: 'DELETE',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("deleteCategory is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
          return res;
    };
    this.loadAccounts =function($scope) {
        var promise = this.listAccounts(this.searchDto);
        promise.then(
            function (data) {
              $scope.accountsInitialized = true;
              $scope.accounts = data;
              return data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    this.loadCategories =function($scope) {
        var promise = this.listCategories(this.searchDto);
        promise.then(
            function (data) {
              $scope.categoriesInitialized = true;
              $scope.categories = data;
              return data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };

    this.listPeriods = function(){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/periods',
           method: 'GET',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("listPeriods is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.loadPeriods =function($scope) {
        var promise = this.listPeriods();
        promise.then(
            function (data) {
              $scope.periods = data;
              return data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    this.listAccountTypes = function(){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/accounttypes',
           method: 'GET',
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("listAccountTypes is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    this.loadAccountTypes =function($scope) {
        var promise = this.listAccountTypes();
        promise.then(
            function (data) {
              $scope.accountTypes = data;
              return data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    this.segregateWithTypes = function($scope, categories) {
        $scope.expCategories = [];
        $scope.incCategories = [];
        $scope.transferCategories = [];
        angular.forEach(categories, function(c) {
            if (c.transactionTypeCode == 'E') {
                $scope.expCategories.push(c);
            } else if (c.transactionTypeCode == 'I') {
                $scope.incCategories.push(c);
            } else if (c.transactionTypeCode == 'T') {
                $scope.transferCategories.push(c);
            }
        });
    };
    this.listSystemCategories = function(page){
      var deferred = $q.defer();
        $http({
           url: $rootScope.hosturl+'/mt/api/domain/systemcategories',
           method: 'POST',
           data: page,
           headers: {'Content-Type': 'application/json', 'tn': $rootScope.access_token}
          }).success(function(data, status, headers, config) {
              console.info("listSystemCategories is successful... ");
              deferred.resolve(data);
          }).error(function(data, status) {
              deferred.reject(data);
          });
          var res = deferred.promise;
        return res;
    };
    
}]);
