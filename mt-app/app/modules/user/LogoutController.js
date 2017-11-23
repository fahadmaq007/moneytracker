'use strict';

angular.module('mt-app')
    .controller('LogoutController', ['dialogs', '$rootScope', '$scope', '$state', '$location', 'UserService',
    function (dialogs, $rootScope, $scope, $state, $location, UserService) {
    console.info("LogoutController");
  $scope.logout=function() {
    console.info("Loggin out...");
    var promise = UserService.logout($scope.user);
    promise.then(
        function (data) {
           console.dir(data);
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
