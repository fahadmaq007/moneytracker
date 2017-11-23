'use strict';

angular.module('mt-app')
    .controller('SystemCategoryController', ['$modalInstance', '$scope', 'ngTableParams','ApplicationService', 'DomainService','ACTION_INDEX',
    function ($modalInstance, $scope, ngTableParams, ApplicationService, DomainService, ACTION_INDEX) {
    console.info("SystemCategoryController");
    $scope.selectedCategories = [];
    $scope.storeList = [];
    $scope.allSelected = false;

    $scope.initSystemCategories = function() {
      if (! $scope.sysCatTableParams) {
        $scope.initSystemCategoriesTable();
      }
    };
    $scope.initSystemCategoriesTable = function() {
      $scope.sysCatTableParams = new ngTableParams({
          page: 1,            // show first page
          count: 10,          
      }, 
      {
          total: 0, // length of data
          getData: function($defer, params) {
            var pageDto = { };
              var page = params.page();
              var count = params.count();
              pageDto.pageNumber = page;
              pageDto.pageSize = count;
              
              var promise = DomainService.listSystemCategories(pageDto);
              promise.then(
                  function (responseData) {
                    if (responseData) {
                        $scope.systemCategories = responseData.contentList;
                        if (responseData.page != null) {
                            pageDto.totalRecords = responseData.page.totalRecords;
                            $scope.sysCatTableParams.total(pageDto.totalRecords);
                        } else {
                          $scope.systemCategories = [];
                          $scope.sysCatTableParams.total(0);
                        }
                    } else {
                      $scope.systemCategories = [];
                      $scope.sysCatTableParams.total(0);
                    }
                    $defer.resolve($scope.systemCategories);
                  },
                  function (reason) {
                      console.log('Failed: ' + reason);
                  }
              );
          }
      });
    };
    $scope.cancel = function(){
        $modalInstance.dismiss('Canceled');
    }; // end cancel
    
    $scope.import = function(){
      var toBeStoredList = [];
      for (var i = 0; i < $scope.selectedCategories.length; i++) {
        var c = $scope.selectedCategories[i];
        console.info(c.name);
        if (c.$$selected) { 
          var childList = [];
          c.action.actionIndex = ACTION_INDEX.NEW;
          toBeStoredList.push(c);
          if (c.children && c.children.length > 0) {
            for (var j = 0; j < c.children.length; j++) {
              var child = c.children[j];
              if (child.$$selected) {
                child.action.actionIndex = ACTION_INDEX.NEW;
                childList.push(child);
              }
            };  
          }
          c.children = childList;
        }
      };
      $modalInstance.close(toBeStoredList);
    };
    $scope.select = function(c) { 
        console.info("select " + c.name); 
        c.$$selected = !c.$$selected;
        if (c.$$selected) {          
          c.defaultAccountId = undefined; 
          $scope.selectedCategories.push(c);
        } else {
          c.$$selected = false;
          $scope.allSelected = false;
          var index = $scope.selectedCategories.indexOf(c);
          if (index > -1) {
              $scope.selectedCategories.splice(index, 1);
          }
        }
        if (c.children && c.children.length > 0) {
          if (c.$$selected) {
            c.$$hideRows = false;
          }
          for (var i = 0; i < c.children.length; i++) {
            var child = c.children[i];
            child.$$selected = c.$$selected;
          };
        }
        console.info("$scope.selectedCategories " + $scope.selectedCategories.length);
        console.dir($scope.selectedCategories);
    };
}]);
