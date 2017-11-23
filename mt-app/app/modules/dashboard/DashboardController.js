'use strict';

angular.module('mt-app')
    .controller('DashboardController', ['$rootScope', '$filter', '$scope', 'hotkeys', '$window',
        'ApplicationService','TransactionService','DomainService',
    function ($rootScope, $filter, $scope, $hotkeys, $window, ApplicationService, TransactionService, DomainService) {
    console.info("Dashboard Controller");
    $scope.searchDto = {
        'noOfTransactions': 5
    };
    $scope.chartTypes = [ {type: 'column', name: 'Column'},
    {type: 'bar', name: 'Bar'}, {type: 'pie', name: 'Pie'} ];
    $scope.selectedChart = $scope.chartTypes[0];
    $scope.historyReportConf =  {
        title: "History",
        color: "#e97f70"
    }
    $scope.expenseReportConf = {
    	title: "Expenses",
    	color: "#e97f70"
    }
    $scope.incomeReportConf = {
    	title: "Income",
    	color: "#60D59C"
    }
    $scope.reports = [$scope.expenseReportConf, $scope.incomeReportConf,
    $scope.historyReportConf]
    $scope.selectedReport = $scope.reports[0];
    $scope.incExpDrilldownSeries = [];
    $scope.incExpSeries = {
        name: 'Expenses',
        data: [],
        colorByPoint: true
    };

    $scope.xAxisData = [];
    $scope.yAxisData = [];
    $scope.chartIncExp = {
        options: {
          chart: {
            type: $scope.selectedChart.type,

          },
          plotOptions: {
            series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: false,
                    style: {
                         'color': '#DDDDDD',
                         'font-size': '1.1em',
                         'font-family': 'inherit',
                         'text-decoration': 'none',
                         'font-weight': 'normal'
                    },
                    format: '{point.y:.f}'
                }
            },
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
          },
          colors: $rootScope.colors,
        },
        title: {
            className: 'chart-title',
            text: $scope.selectedReport.title + ' Chart'
        },
        subtitle: {
            className: 'chart-subtitle'
        },
        xAxis: {
            gridLineColor: '#707073',
            labels: {
                rotation: -25,
                className: 'chart-label'
            },
            title: {
                className: 'chart-label'
            },
            type: 'category'
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Amount'
            },
            labels: {
                className: 'chart-label'
            }
        },
        series: [$scope.incExpSeries],
        drilldown: {
            series: $scope.incExpDrilldownSeries
        },
        loading: false,
        legend: {
            enabled: true
        },
        tooltip: {
            pointFormat: '<b>{point.y:.2f} ' + $rootScope.defaultCurrency +'</b>'
        },
        size: {
           height: 400
        },
    };

    $scope.init=function() {
      console.info("DC:init");
      $scope.thisMonth();
    };
    $scope.showReport=function() {
      $scope.initChart();
      $scope.showBudget();
    };
    $scope.showBudget = function() {
      var data = {
          'fromDate': $scope.searchDto.fromDate,
          'toDate': $scope.searchDto.toDate
      };
      console.info("showBudget")
      console.dir(data);
      $rootScope.$broadcast("budgetSearchChanged", data);
    }
    $scope.changeReport=function(report) {
      if (report.title == 'History') {
        $scope.historyReport();
      } if (report.title == 'Income') {
        $scope.incomeReport();
      } else {
        $scope.expenseReport();
      }
      $scope.selectedReport = report;
      //$scope.getIncomeExpenseTotal();
    };
    $scope.initChart=function() {
      $scope.changeReport($scope.selectedReport);
    	//$scope.getIncomeExpenseTotal();
    };
    $scope.getIncomeExpenseTotal = function() {
    	var totalPromise = TransactionService.getIncomeExpenseTotal($scope.searchDto);
        totalPromise.then(
            function (data) {
                console.dir(data);
                if (data != undefined || data != null) {
                    $scope.incomeExpenseTotal = data;
                }
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.openFromDate = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.fromDateOpened = true;
      };
    $scope.openToDate = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.toDateOpened = true;
      };
    $scope.changeChartType = function(chart) {
      $scope.selectedChart = chart;
    	$scope.chartIncExp.options.chart.type = chart.type;
      $scope.chartHistory.options.chart.type = chart.type;
    };
    $scope.reflow = function () {
        $scope.$broadcast('highchartsng.reflow');
    };
    $scope.historyReport = function() {
        var promise = TransactionService.listIncExpHistoryReport($scope.searchDto);
        promise.then(
            function (data) {
                $scope.prepareHistoryChart(data);
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.expenseReport = function() {
    	var promise = TransactionService.expenseReportByCategoryTree($scope.searchDto);
        promise.then(
            function (data) {
            	//$scope.selectedReport = $scope.expenseReportConf;
            	$scope.prepareChartData(data);
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.incomeReport = function() {
    	var promise = TransactionService.incomeReport($scope.searchDto);
        promise.then(
            function (data) {
            	//$scope.selectedReport = $scope.incomeReportConf;
            	$scope.prepareChartData(data);
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };

    $scope.prepareChartData = function(data) {
      if (data != undefined || data != null) {
       $scope.incExpSeries.name = $scope.selectedReport.title;
       $scope.incExpSeries.color = $scope.selectedReport.color;
       $scope.chartIncExp.xAxis.title.text = $scope.selectedReport.title;
       $scope.chartIncExp.title.text = $scope.selectedReport.title + ' Chart';
        $scope.incExpSeries.data = [];
        //$scope.incExpDrilldownSeries = [];
        console.info("before");
        console.info(JSON.stringify($scope.incExpDrilldownSeries));
        for (var i = 0; i < data.length; i++) {
            var tranDto = data[i];
            //$scope.chartIncExp.xAxis.categories.push(tranDto.catType);
            $scope.incExpSeries.data.push({name: tranDto.catType,
                y: tranDto.amount,
                drilldown: tranDto.catType
            });
            if (tranDto.children) {
                var drilldownData = [];
                for (var j = 0; j < tranDto.children.length; j++) {
                    var child = tranDto.children[j];
                    drilldownData.push([child.catType, child.amount]);

                }

                $scope.incExpDrilldownSeries.push({
                    id: tranDto.catType,
                    name: tranDto.catType,
                    data: drilldownData
                });
            }
        }
        console.info("after")
        console.info(JSON.stringify($scope.incExpSeries));
        console.info(JSON.stringify($scope.incExpDrilldownSeries));
      }
    };
    $scope.today = new Date();
    $scope.thisMonth = function() {
      $scope.setRange($scope.today);
    };
    $scope.prevMonth = function() {
      var prevMonth = ApplicationService.prevMonth($scope.searchDto.fromDate);
      $scope.setRange(prevMonth);
    };
    $scope.nextMonth = function() {
      var nextMonth = ApplicationService.nextMonth($scope.searchDto.toDate);
      $scope.setRange(nextMonth);
    };
    $scope.setRange = function(date) {
      console.info("date range");
      var fromDate = angular.copy(date);
      fromDate.setDate(1);
      $scope.searchDto.fromDate = fromDate;
      var toDate = angular.copy(date);
      var days = toDate.getDaysInMonth();
      toDate.setDate(days);
      console.info(fromDate);
      console.info(toDate);
      $scope.searchDto.toDate = toDate;
      $scope.showReport();
    };

    $scope.historyIncSeries = {
        name: 'Income',
        data: [],
        color: $rootScope.successColor
    };
    $scope.historyExpSeries = {
        name: 'Expenses',
        data: [],
        color: $rootScope.dangerColor
    };
    $scope.chartHistory = {
        options: {
          chart: {
            type: $scope.selectedChart.type,
          },
          plotOptions: {
            series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: false,
                    style: {
                         'color': '#DDDDDD',
                         'font-size': '1.1em',
                         'font-family': 'inherit',
                         'text-decoration': 'none',
                         'font-weight': 'normal'
                    },
                    format: '{point.y:.f}'
                }
            },
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
          },
          colors: $rootScope.colors,
        },
        title: {
            className: 'chart-title',
            text: 'History Chart'
        },
        subtitle: {
            className: 'chart-subtitle'
        },
        xAxis: {
            gridLineColor: '#707073',
            labels: {
                rotation: -25,
                className: 'chart-label'
            },
            title: {
                className: 'chart-label'
            },
            type: 'category'
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Amount'
            },
            labels: {
                className: 'chart-label'
            }
        },
        series: [$scope.historyIncSeries, $scope.historyExpSeries],
        loading: false,
        legend: {
            enabled: true
        },
        tooltip: {
            pointFormat: '<b>{point.y:.2f} ' + $rootScope.defaultCurrency +'</b>'
        },
        size: {
           height: 400
        },
    };
    $scope.prepareHistoryChart = function(data) {
      $scope.historyExpSeries.data = [];
      $scope.historyIncSeries.data = [];
      for (var i = 0; i < data.length; i++) {
        var tranDto = data[i];
        var type = tranDto.tranType;
        var series = undefined;
        if (type == 'E') {
            series = $scope.historyExpSeries;
        } else if (type == 'I') {
            series = $scope.historyIncSeries;
        } else {
          continue;
        }
        console.dir(tranDto)


        series.data.push({
          name: tranDto.period,
          y: tranDto.amount
        });
      }
      console.dir($scope.historyExpSeries);
      console.dir($scope.historyIncSeries);
    };
    $scope.showSetupProgress = function() {
      console.info("showSetupProgress");            
      var promise = ApplicationService.listSetupActivities();
        promise.then(
            function (data) {
              if (data) {
                $scope.setupActivities = data;
                var total = 0;
                var totalCompleted = 0;
                for (var i = 0; i < data.length; i++) {
                  var a = data[i];
                  total += data[i].points; 
                  if (a.done) {
                   totalCompleted += data[i].points; 
                  }
                };
                var completion = totalCompleted / total;
                $scope.activitiesCompletion = completion;
              }
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
}]);
