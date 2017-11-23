var globalErrors = new angular.module('http-error-handling', [])
.config(function($provide, $httpProvider, $compileProvider) {
    var elementsList = $();
    var successClass = 'http-success-message';
    var validationClass = 'http-error-validation-message';
    var errorClass = 'http-error-message';
    var showMessage = function(content, cl, time) {
      console.info("showMessage " + content);
      
    };

    var interceptor = ['$q', '$cacheFactory', '$timeout', '$rootScope', function ($q, $cacheFactory, $timeout, $rootScope) {

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
        var exception = "Exception";
        var dot = ".";
        var code = data.code;
        if (code) {
          if (endsWith(code, exception) && contains(code, dot)) {
            console.error("there is an exception in http response: " + data.message);
            console.dir(response);
            showMessage(data.message, 
                    errorClass, 8000);
          }
        }
      }

      return {
        'request': function(config) {
            if (!isCached(config)) {
              
            }
            return config;
        },

        'response': function(response) {
          if (!isCached(response.config)) {
            if (response) {
              handleException(response);  
            }            
          }
          return response;
        },

        'responseError': function(rejection) {
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

    $compileProvider.directive('httpErrorMessages', function() {
      return {
        link: function(scope, element, attrs) {
          elementsList.push($(element));
        }
      };
    });
});

