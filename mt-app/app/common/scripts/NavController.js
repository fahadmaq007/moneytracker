'use strict';

angular.module('mt-app')
    .controller('NavController', ['$rootScope','$scope',
    function ($rootScope, $scope) {

    $scope.dashboardItem = {
        active: false,
        url:'dashboard',
        title: 'Dashboard',
        logoClass: 'fa fa-tachometer',
    };

    $scope.transactionsItem = {
        active: false,
        url:'transaction',
        title: 'Transactions',
        logoClass: 'fa fa-money',
    };

    $scope.budgetItem = {
        active: false,
        url:'budget',
        title: 'Budget',
        logoClass: 'fa fa-area-chart',
    };

    $scope.importBankStatementItem = {
        active: false,
        url:'statements',
        title: 'Import Statements',
        logoClass: 'fa fa-download',
    };

    $scope.domainItem = {
        active: false,
        url:'domain',
        title: 'Domain',
        logoClass: 'fa fa-tags',
    };

    $scope.reportsItem = {
        active: false,
        url:'reports',
        title: 'Reports',
        logoClass: 'fa fa-pie-chart',
        disabled: true
    };

    $scope.alertsItem = {
        active: false,
        url:'alerts',
        title: 'Alerts',
        logoClass: 'fa fa-bell',
        disabled: true
    };
    $scope.userPrefsItem = {
        active: false,
        url:'preferences',
        title: 'User Preferences',
        logoClass: 'fa fa-cog',
    };
    $scope.logoutItem = {
        active: false,
        url:'logout',
        title: 'Logout',
        logoClass: 'fa fa-sign-out'
    };
    $scope.aboutUsItem = {
        active: false,
        url:'aboutus',
        title: 'About US',
        logoClass: 'fa fa-phone-square',

    };

    $scope.navItems = [
        $scope.dashboardItem,
        $scope.transactionsItem,
        $scope.budgetItem,
        $scope.importBankStatementItem,
        $scope.domainItem,
        //$scope.reportsItem,
        // $scope.alertsItem,
        /*$scope.userPrefsItem,

        $scope.aboutUsItem,
        $scope.logoutItem*/
    ];
    $scope.selectNav = function(navItem) {
        if ($scope.prevItem) {
            $scope.prevItem.active = false;
        }
    	$scope.selectedId = navItem.id;
    	$rootScope.selectedNavTitle = navItem.title;
        navItem.active = true;
        $scope.prevItem = navItem;
    };

    $scope.selectNav($scope.dashboardItem);
}]);
