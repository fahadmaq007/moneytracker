'use strict';
angular.module('mt-app')
.controller('ApplicationController',['$cookieStore','$rootScope', 'dialogs', '$state',
    function($cookieStore,rootScope, dialogs,$state) {

    console.info("ApplicationController ");
    rootScope.$on('exception', function(event, data) {
        console.log("exception!");
        dialogs.error(undefined,data.message);
    });

    var launch = function() {
        var userDto = $cookieStore.get("userDto");
        console.info("cookie ");
        console.dir(userDto);
        if (userDto) {
            rootScope.loggedInUser = userDto;
            rootScope.access_token = userDto.token;
        }
    	console.info("launching");
        console.dir(rootScope.loggedInUser);
		  if (rootScope.loggedInUser != undefined) {
	    	$state.go('dashboard');
	    } else {
	    	$state.go('/');
	    }
    };

    launch();

    rootScope.$on('launch', function(event, data) {
    	console.info("launch listener " + data);
    	console.dir(data);
        launch();
    });
}]);
