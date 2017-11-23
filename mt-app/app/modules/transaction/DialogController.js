'use strict';

angular.module('mt-app')
    .controller('DialogController', function ($scope,$modalInstance,data) {
    $scope.t = data.transaction;
    $scope.accounts = data.accounts;
    $scope.expCategories = data.expCategories;
    $scope.incCategories = data.incCategories;

    $scope.$types = [{name: 'Expense', typeClass: 'expTexture'},
    {name: 'Income', typeClass: 'incTexture'},
    {name: 'Transfer', typeClass: 'tnfrTexture'}];
    if ($scope.t.category.transactionType.code== 'T') {
        $scope.$type = $scope.$types[2].name;    
    } else if ($scope.t.category.transactionType.code== 'I') {
        $scope.$type = $scope.$types[1].name;    
    } else {
        $scope.$type = $scope.$types[0].name; 
    }
    $scope.cancel = function(){
        $modalInstance.dismiss('Canceled');
    }; // end cancel
    
    $scope.save = function(){
        $modalInstance.close(data);
    }; // end save   

    $scope.openDate = function($event, t) {
        $event.preventDefault();
        $event.stopPropagation();

        t.$$dateOpened = true;
    }; 
    $scope.changeType=function(type) {
        $scope.$type = type.name;
    };
});
