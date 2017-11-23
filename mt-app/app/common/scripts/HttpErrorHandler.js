var errorHandler = new angular.module('http-error-handling', [])
.config(['$provide', '$httpProvider', '$compileProvider',function($provide, $httpProvider, $compileProvider) {    var interceptor = ['$q', '$cacheFactory', '$timeout', '$rootScope', function ($q, $cacheFactory, $timeout, $rootScope) {

      function setComplete() {

      }

      /**
       * Determine if the response has already been cached
       * @param  {Object}  config the config option from the request
       * @return {Boolean} retrns true if cached, otherwise false
       */
      function isCached(config) {
        var cache;
        var defaults = $httpProvider.defaults;

        if (config.method !== 'GET' || config.cache === false) {
          config.cached = false;
          return false;
        }

        if (config.cache === true && defaults.cache === undefined) {
          cache = $cacheFactory.get('$http');
        } else if (defaults.cache !== undefined) {
          cache = defaults.cache;
        } else {
          cache = config.cache;
        }

        var cached = cache !== undefined ?
          cache.get(config.url) !== undefined : false;

        if (config.cached !== undefined && cached !== config.cached) {
          return config.cached;
        }
        config.cached = cached;
        return cached;
      }

      function endsWith(str, suffix) {
          return str.indexOf(suffix, str.length - suffix.length) !== -1;
      }
      function contains(str, what) {
          return str.indexOf(what) > -1;
      }
      function handleException(response) {
        if (!response)
          return;
        var data = response.data;
        if (! data) {
          return;
        }
        var exception = "BusinessException";
        var dot = ".";
        var code = data.code;
        if (code) {
          if (endsWith(code, exception) && contains(code, dot)) {
            console.error("there is a BusinessException: " + data.message);
            $rootScope.$broadcast("exception", data);
          } else {
            console.error("there is an exception in http response: " + data.message);
          }
        }
      }

      return {
        'response': function(response) {
          if (!isCached(response.config)) {
            if (response) {
              handleException(response);
            }
          }
          return response;
        },

        'responseError': function(rejection) {
          console.info("rejection");
          console.dir(rejection);
            if (!isCached(rejection.config)) {
                if (rejection) {
                  var data = rejection.data;
                  handleException(rejection);
                }
            }
          return $q.reject(rejection);
        }
      };
    }];

    $httpProvider.interceptors.push(interceptor);
}]);
