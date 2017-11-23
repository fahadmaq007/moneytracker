'use strict';

angular.module('mt-app')
    .controller('PreferencesDialogController', function ($rootScope,$scope,$modalInstance,data, UserService) {
    $scope.cancel = function(){
        $modalInstance.dismiss('Canceled');
    }; // end cancel
    $scope.busy = false;

    $scope.login=function() {
        $scope.busy = true;

        var promise = UserService.login($scope.user);
        promise.then(
            function (data) {
              $scope.busy = false;
              console.dir(data);
              if (data.message) {
                $scope.error = true;
                $scope.errorMessage = data.message;
              } else {
                $modalInstance.close(data);
              }
            },
            function (reason) {
                $scope.error = true;
                $rootScope.loggedIn = false;
            }
        );
        return promise;
      };

    $scope.register=function() {
        $scope.busy = true;
        var promise = UserService.register($scope.user);
        promise.then(
            function (data) {
              $scope.busy = false;
               if (data.message) {
                $scope.error = true;
                $scope.errorMessage = data.message;
               } else {
                $modalInstance.close($scope.user);
               }
            },
            function (reason) {
                $scope.error = true;
            }
        );
    };
});
