'use strict';

angular.module('mt-app')
    .controller('LoginController', ['$cookieStore','dialogs', '$rootScope', '$scope', '$state', '$location', 'UserService',
    function ($cookieStore, dialogs, $rootScope, $scope, $state, $location, UserService) {
    console.info("LoginController");
  $scope.errorMessage = "There was a problem logging in. Please try again.";
  $scope.showLoginDialog=function() {
    var dlg = dialogs.create('modules/user/login.html',
        'LoginDialogController',null,
        'sm');
    dlg.result.then(function(data){
        $cookieStore.put("userDto", data);
        $rootScope.$broadcast("launch", data);
        console.info("login");
        console.dir(data);
    },function(){
        console.info("cancel");
    });
  };

  $scope.showRegisterDialog=function() {
    var dlg = dialogs.create('modules/user/register.html',
        'LoginDialogController',null,
        'sm');
    dlg.result.then(function(data){
        console.info("register");
        console.dir(data);
    },function(){
        console.info("cancel");
    });
  };

  $scope.showPreferencesDialog=function() {
    var dlg = dialogs.create('modules/user/preferences.html',
        'PreferencesDialogController',null,
        'lg');
    dlg.result.then(function(data){
        console.info("showPreferencesDialog");
        console.dir(data);
    },function(){
        console.info("cancel");
    });
  };

  $scope.logout=function() {
        console.info("Loggin out...");
        var promise = UserService.logout($rootScope.loggedInUser);
        promise.then(
            function (data) {
               console.dir(data);
                $cookieStore.remove("userDto");
                $rootScope.loggedInUser = undefined;
                $rootScope.access_token = undefined;
                $rootScope.$broadcast("launch", $rootScope.loggedInUser);
            },
            function (reason) {
                $scope.error = true;
            }
        );
      };
}]);
