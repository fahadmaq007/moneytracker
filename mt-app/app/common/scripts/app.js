'use strict';
var app = angular.module('mt-app', [
    'ui.bootstrap',
    'ngSanitize',
    'ngAutocomplete',
    'ui.router',
    'LocalStorageModule',
    'services.config',
    'ui.select2',
    'highcharts-ng',
    'angularMoment',
    'xeditable',
    'ngTable',
    'cfp.hotkeys',
    'angularFileUpload',
    'dialogs.main',
    'http-error-handling',
    'ngCookies',
    'ngBusy',
    'angular-progress-arc'
]);
app.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});
app.config(function (datepickerConfig) {
  datepickerConfig.showWeeks = false;
});
app.directive('datepickerPopup', ['datepickerPopupConfig', 'dateParser', 'dateFilter', function (datepickerPopupConfig, dateParser, dateFilter) {
    return {
        'restrict': 'A',
        'require': '^ngModel',
        'link': function ($scope, element, attrs, ngModel) {
            var dateFormat;

            //*** Temp fix for Angular 1.3 support [#2659](https://github.com/angular-ui/bootstrap/issues/2659)
            attrs.$observe('datepickerPopup', function(value) {
                dateFormat = value || datepickerPopupConfig.datepickerPopup;
                ngModel.$render();
            });

            ngModel.$formatters.push(function (value) {
                return ngModel.$isEmpty(value) ? value : dateFilter(value, dateFormat);
            });
        }
    };
}]);
app.config(['dialogsProvider','$translateProvider',
  function(dialogsProvider,$translateProvider){
    dialogsProvider.useBackdrop('static');
    dialogsProvider.useEscClose(true);
    dialogsProvider.useCopy(false);
}]);
app.directive('fixedTableHeaders', ['$timeout', function($timeout) {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
      $timeout(function() {
          var container = element.parentsUntil(attrs.fixedTableHeaders);
          element.stickyTableHeaders({ scrollableArea: container, "fixedOffset": 2 });
      }, 0);
    }
  }
}]);
app.directive('scrollTo', function ($location, $anchorScroll) {
  return function(scope, element, attrs) {

    element.bind('click', function(event) {
        event.stopPropagation();
        var off = scope.$on('$locationChangeStart', function(ev) {
            off();
            ev.preventDefault();
        });
        var location = attrs.scrollTo;
        $location.hash(location);
        $anchorScroll();
    });
  }
});
app.directive('focus', function () {
  return function (scope, element, attrs) {
     element.focus();
  }
});
app.directive('onCarouselChange', function ($parse) {
  return {
    require: 'carousel',
    link: function (scope, element, attrs, carouselCtrl) {
      var fn = $parse(attrs.onCarouselChange);
      var origSelect = carouselCtrl.select;
      carouselCtrl.select = function (nextSlide, direction) {
        if (nextSlide !== this.currentSlide) {
          fn(scope, {
            nextSlide: nextSlide,
            direction: direction,
          });
        }
        return origSelect.apply(this, arguments);
      };
    }
  };
});
// app.config(function(cfpLoadingBarProvider) {
//     cfpLoadingBarProvider.includeSpinner = true;
// });
angular.module('mt-app').constant('angularMomentConfig', {
    preprocess: 'unix', // optional
    timezone: 'Europe/London' // optional
});
app.run(function(editableOptions, editableThemes) {
  editableThemes.bs3.inputClass = 'input-sm';
  editableThemes.bs3.buttonsClass = 'btn-sm';
  editableOptions.theme = 'bs3';
});
app.directive('resize', function ($rootScope, $window) {
    return function (scope, element) {
        var w = angular.element($window);
        scope.getWindowDimensions = function () {
            return {
                'h': w.height(),
                'w': w.width()
            };
        };
        scope.$watch(scope.getWindowDimensions, function (newValue, oldValue) {
            $rootScope.screenHeight = newValue.h;
            $rootScope.screenWidth = newValue.w;
            $rootScope.contentPanelHeight = newValue.h - 140;
            scope.style = function () {
                return {
                    'height': newValue.h + 'px',
                    'width': newValue.w + 'px'
                };
            };

        }, true);
        w.bind('resize', function () {
            scope.$apply();
        });
    };
});
app.config(['$tooltipProvider', function($tooltipProvider) {
  $tooltipProvider.options({animation: false});
}]);
app.config(['$urlRouterProvider', '$stateProvider', function ($urlRouterProvider, $stateProvider) {
    $urlRouterProvider.otherwise('/');
    $stateProvider
        .state('/', {
           url: '/home',
           templateUrl: 'modules/home/landingpage.html',
           controller: 'LandingPageController'
        })
        .state('features', {
           url: '/features',
           templateUrl: 'modules/home/features.html',
           controller: 'LandingPageController'
        })
        .state('dashboard', {
           url: '/dashboard',
           templateUrl: 'modules/dashboard/dashboard.html',
           controller: 'DashboardController'
        })
       .state('transaction', {
           url: '/transaction',
           templateUrl: 'modules/transaction/transactions.html',
           controller: 'TransactionController'
        })
       .state('domain', {
           url: '/domain',
           templateUrl: 'modules/domain/domain.html',
           controller: 'DomainController'
        })
       .state('budget', {
           url: '/budget',
           templateUrl: 'modules/budget/budget.html',
           controller: 'BudgetController'
        })
       .state('statements', {
           url: '/statements',
           templateUrl: 'modules/bankstatements/bankstatements.html',
           controller: 'BankStatementController'
        })
       .state('logout', {
           url: '/logout',
           templateUrl: '',
           controller: 'LogoutController'
        })
}]);
app.config(['$sceDelegateProvider', function ($sceDelegateProvider) {
    $sceDelegateProvider.resourceUrlWhitelist(['.*']);
    $sceDelegateProvider.resourceUrlWhitelist(['^(?:http(?:s)?:\/\/)?(?:[^\.]+\.)?$', 'self']);
}]);
app.config(['localStorageServiceProvider', function (localStorageServiceProvider) {
    localStorageServiceProvider.setPrefix('bim');
}]);

app.filter('unsafe', function ($sce) {
    return function (val) {
        return $sce.trustAsHtml(val);
    };
});
/*app.run(['$rootScope', 'ApplicationService', function ($rootScope, ApplicationService) {
    ApplicationService.init();
}]);*/
app.constant('keyCodes', {
    esc: 27,
    space: 32,
    enter: 13,
    tab: 9,
    backspace: 8,
    shift: 16,
    ctrl: 17,
    alt: 18,
    capslock: 20,
    numlock: 144
})
    .directive('keyBind', ['keyCodes', function (keyCodes) {
        function map(obj) {
            var mapped = {};
            for (var key in obj) {
                var action = obj[key];
                if (keyCodes.hasOwnProperty(key)) {
                    mapped[keyCodes[key]] = action;
                }
            }
            return mapped;
        }

        return function (scope, element, attrs) {
            var bindings = map(scope.$eval(attrs.keyBind));
            element.bind("keydown keypress", function (event) {
                if (bindings.hasOwnProperty(event.which)) {
                    scope.$apply(function () {
                        scope.$eval(bindings[event.which]);
                    });
                }
            });
        };
    }]);
app.constant('ACTION_INDEX', {
    NEW: 1,
    UPDATE: 2,
    DELETE: 3
})
app.run(function ($rootScope, configuration) {
    $rootScope.hosturl = configuration.hosturl;
    $rootScope.busyMessage = "Loading ...";
    $rootScope.defaultNumberStep = 50;
    $rootScope.defaultCurrency = "Rs. ";
    $rootScope.defaultCurrencyClass = "fa fa-inr";
    $rootScope.dateFormat = "yyyy-MMM-dd";
    $rootScope.dateTimeFormat = "yyyy-MMM-dd h:mm a";
    $rootScope.minDate="2013-Jan-01",
    $rootScope.months = [{"id": 1,"value":'Jan'}, {"id": 2,"value":'Feb'},
    {"id": 3,"value":'Mar'},
    {"id": 4,"value":'Apr'}, {"id": 5,"value":'May'}, {"id": 4,"value":'Jun'},
    {"id": 7,"value":'Jul'}, {"id": 8,"value":'Aug'}, {"id": 9,"value":'Sep'},
    {"id": 10,"value":'Oct'}, {"id": 11,"value":'Nov'}, {"id": 12,"value":'Dec'}];
    $rootScope.select2Options = {
        allowClear:true,
        focus: true,
    };
    $rootScope.ACTION_NEW = 1;
    $rootScope.ACTION_UPDATE = 2;
    $rootScope.successColor = "#60D59C";
    $rootScope.dangerColor = "#E97F70";
    $rootScope.colors = ["#70aead", "#8085e9", "#353230", $rootScope.successColor, "#6f5757", "#4f5d6f", "#f4c5b5", 
      "#f7a35c", "#ba3c3d", "#91adce","#fdb77b","#69a8bb","#e7736b", "#b4366f",$rootScope.dangerColor,
      "#dfcb7a", "#314e7e", "#b89a1c"];
    $rootScope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };
});
app.run(function ($http, $rootScope) {
    $http.get('common/scripts/currencymap.json').success(function(response) {
        if (response) {
          $rootScope.currencies = [];
          console.info(response);
          var result = angular.fromJson(response);
          $.each(result, function(k, v) {
              $rootScope.currencies.push(v);
          });
          console.info("total currencies " + $rootScope.currencies.length);
        }  
    });
});
app.directive('ngBlur', function () {
  return function (scope, elem, attrs) {
    elem.bind('blur', function () {
      scope.$apply(attrs.ngBlur);
    });
  };
});
