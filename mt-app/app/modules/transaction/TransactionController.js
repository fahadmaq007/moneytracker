'use strict';

angular.module('mt-app')
    .controller('TransactionController', ['$rootScope', 'dialogs', '$filter', '$scope', 'hotkeys', '$window',
        'ACTION_INDEX', 'ApplicationService','TransactionService','DomainService', 'ngTableParams',
    function ($rootScope, dialogs, $filter, $scope, $hotkeys, $window, ACTION_INDEX, ApplicationService, TransactionService, DomainService, ngTableParams) {
    console.info("Transactions Controller ");
    $scope.today = new Date();
    $scope.transactions = [];
    $scope.selectedTransactions = [];
    $scope.storeList = [];

    $scope.allSelected = false;
    $scope.contentAreaHeight = $rootScope.contentPanelHeight;
    $scope.selectedTransaction;
    $scope.chartData = [];
    $scope.selectedCategoryNames = [];
    
    
    $scope.selectedMonths = []; //moment().format('MMM');
    $scope.selectedMonth = '';
    $scope.newTransaction = { 'onDate': $scope.today, 'category': {}, 'account': {},
    'action': {'actionIndex' : ACTION_INDEX.NEW} };
    $scope.domainDto = {

    };
    $scope.dateSelectOptions = angular.copy($rootScope.select2Options);
    $scope.dateSelectOptions.maximumSelectionSize = 2;

    $scope.openDate = function($event, t) {
        $event.preventDefault();
        $event.stopPropagation();

        t.$$dateOpened = true;
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
    $scope.aggFunc = function (row) {
        var total = 0;
        angular.forEach(row.children, function(row) {
            total+=row.entity.amount;
        });
        return total.toString();
    };

    $scope.entryMaybePlural = function (row) {
        if(row.children.length>1) {
            return "transactions ";
        } else
            return "transaction ";
    };
    $scope.fields=['onDate','description', 'category.name','account.name','amount'];

    $scope.getClass = function(transaction) {
        if (transaction.category == null) {
            return "";
        }
        if (transaction.category.transactionTypeCode == 'E') {
            return "expense";
        }
        return "income";
    };
    $scope.clearSelectedMonths = function() {
        console.dir($scope.selectedMonths);
        $scope.selectedMonths = [];
    };
    $scope.init = function () {   
        console.info("init");
        $scope.loadCategories();
        $scope.loadAccounts();
        
        $scope.searchDto = {
            'page': { 'pageNumber': 1, 'pageSize': 10, 'totalRecords' : 0},
            'reportBy': 'OVERALL',
            'noOfTransactions': 5
        };
        $scope.thisMonth();
        $scope.tableParams = new ngTableParams({
            page: $scope.searchDto.page.pageNumber,            // show first page
            count: $scope.searchDto.page.pageSize,           // count per page
            sorting: {
                onDate: 'desc'     // initial sorting
            }
        }, {
            total: $scope.searchDto.page.totalRecords, // length of data
            getData: function($defer, params) {
                $scope.reset();
                var page = params.page();
                var count = params.count();
                $scope.searchDto.page.pageNumber = page;
                $scope.searchDto.page.pageSize = count;
                var orderBy = params.orderBy();
                var order = "DESC";
                if (orderBy != undefined && orderBy.length > 0) {
                    var sign = orderBy[0].substring(0, 1);
                    orderBy = orderBy[0].substring(1);
                    if (sign == '+') {
                        order = "ASC";
                    }
                    $scope.searchDto.orderByList = [];
                    var orderBySpec = { "propertyName" : orderBy,
                    "operation": order};
                    $scope.searchDto.orderByList.push(orderBySpec);
                }
                $scope.requestServerPage(page, $defer);
            }
        });
        

        $hotkeys.add({
            combo: 'alt+a',
            description: 'Add Transaction',
            callback: function() {
              $scope.add();
            }
          });
        $hotkeys.add({
            combo: 'alt+s',
            description: 'Save All',
            callback: function() {
              $scope.saveAll();
            }
          });
        
    };
    $scope.reset = function() {
        var toDate = $scope.searchDto.toDate;
        $scope.isCurrentMonth = ApplicationService.isCurrentMonth(toDate);
        $scope.selectedTransactions = [];        
    };
    $scope.saveAll = function() {
        console.info("saveAll");
        var list = $scope.storeList;
        if (!list || list.length == 0) {
            return;
        }
        var indexes = [];
        for (var i = 0; i < list.length; i++) {
            var t = list[i];
            var index = $scope.transactions.indexOf(t);
            indexes.push(index);
            if (i == 0) {
                $scope.previousTranDate = t.onDate;
            }
        };
        $scope.selectedTransactions = [];
        $scope.allSelected = false;
        var promise = TransactionService.storeTransactions(list);
        promise.then(
            function (data) {
                for (var i = 0; i < $scope.storeList.length; i++) {
                    var t = data[i];
                    var index = indexes[i];
                    console.info("index: " + index + " " + t.description);                    
                    if (t.messageType == 'success') {
                        $scope.storeList.splice(i, 1);
                        indexes.splice(i, 1);
                        data.splice(i, 1);
                        i--;
                    } else {
                        t.$$edit = true;
                        $scope.storeList[i] = t;
                        t.action.actionIndex = ACTION_INDEX.NEW;
                    }
                    $scope.transactions[index] = t;
                };                
                console.info("$scope.storeList: " + $scope.storeList.length);
            },
            function (reason) {
                t.$$edit = true;
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.initStats=function() {
        var promise = TransactionService.listTopExpenseCategories($scope.searchDto);
        promise.then(
            function (data) {
                if (data != undefined || data != null) {
                    $scope.topExpensesData = data;
                }
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
        var promise = TransactionService.listTopIncomeCategories($scope.searchDto);
        promise.then(
            function (data) {
                if (data != undefined || data != null) {
                    $scope.topIncomeData = data;
                }
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
        var promise = TransactionService.getExpenseTotalByReport($scope.searchDto.reportBy);
        promise.then(
            function (data) {
                if (data != undefined || data != null) {
                    $scope.expenseTotal = data;
                }
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
        var promise = TransactionService.getIncomeTotalByReport($scope.searchDto.reportBy);
        promise.then(
            function (data) {
                if (data != undefined || data != null) {
                    $scope.incomeTotal = data;
                }
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.loadTransactions =function() {
        var promise = TransactionService.listTransactions($scope.searchDto);
        return promise;
    };
    $scope.loadCategories =function() {
        var promise = DomainService.listCategories($scope.domainDto);
        promise.then(
            function (data) {
                $scope.categories = data;
                $scope.segregateWithTypes(data);
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.loadAccounts =function() {
        var promise = DomainService.listAccounts($scope.domainDto);
        promise.then(
            function (data) {
                $scope.accounts = data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.search = function() {
        $scope.storeList = [];
        $scope.tableParams.page(0);
        $scope.tableParams.reload();
    };
    $scope.footerDetail = function() {
        var footerText;
        var totalExp = 0;
        var totalInc = 0;
        angular.forEach($scope.transactions, function(t) {
            if (t.category == undefined) {
                return;
            }
            if (t.category.transactionTypeCode == 'E') {
                totalExp+=t.amount;
            } else if (t.category.transactionTypeCode == 'I') {
                totalInc+=t.amount;
            }
        });
        footerText = "Total Income: " + totalInc + ", Total Expenses: " + totalExp;
        return footerText;
    };
    $scope.footerDetailClass = function() {
        return defaultFooterDetail;
    };
    $scope.clearCategories = function() {
        $scope.searchDto.categoryIds = [];
        $scope.search();
    };
    $scope.clearAccounts = function() {
        $scope.searchDto.accountIds = [];
        $scope.search();
    };
    $scope.segregateWithTypes = function(categories) {
        $scope.expCategories = [];
        $scope.incCategories = [];
        $scope.transferCategories = [];
        angular.forEach($scope.categories, function(c) {
            if (c.transactionTypeCode == 'E') {
                $scope.expCategories.push(c);
            } else if (c.transactionTypeCode == 'I') {
                $scope.incCategories.push(c);
            } else if (c.transactionTypeCode == 'T') {
                $scope.transferCategories.push(c);
            }
        });
    };
    $scope.updateRow = function(row) {
        console.info(row);
    };
    $scope.updateAccount = function(transaction) {
        var acctId = transaction.accountId;
        if (acctId != undefined) {
            var account = $scope.getAccount(acctId);
            transaction.account = account;
            console.dir(transaction);
        }
    };
    $scope.getAccount = function(acctId) {
        var account = null;
        for (var i = 0; i < $scope.accounts.length; i++) {
            var a = $scope.accounts[i];
            if (acctId == a.id) {
                account = a;
                break;
            }
        }
        return account;
    };
    $scope.updateCategory = function(transaction) {
        var categoryId = transaction.categoryId;
        if (categoryId != undefined) {
            var category = $scope.getCategory(categoryId);
            transaction.category = category;
            console.dir(transaction);
        }
    };
    $scope.getCategory = function(id) {
        var category = null;
        for (var i = 0; i < $scope.categories.length; i++) {
            var c = $scope.categories[i];
            if (id == c.id) {
                category = c;
                break;
            }
        }
        return category;
    };
    $scope.$watch('searchDto.page', function() {
        var page = $scope.searchDto.page;
        console.dir(page);
        if (page != undefined) {
            $scope.totalitemNames = page.totalRecords;
            $scope.itemNamesPerPage=page.pageSize;
            $scope.numPages = Math.round(page.totalRecords / page.pageSize);
        }
    });
    $scope.requestServerPage = function(pageNumber, $defer) {        
        $scope.selectedTransactions = [];
        $scope.allSelected = false;
        if (pageNumber == undefined) {
            pageNumber = 0;
        }
        var page = $scope.searchDto.page;
        page.pageNumber = pageNumber;
        var promise = $scope.loadTransactions();
        promise.then(
            function (responseData) {
                $scope.tranInitialized = true;
              console.info("trans");
                console.dir(responseData);
                if (responseData) {
                    $scope.transactions = responseData.contentList;
                    if (responseData.page != null) {
                        $scope.searchDto.page.totalRecords = responseData.page.totalRecords;
                        $scope.tableParams.total($scope.searchDto.page.totalRecords);
                    } else {
                      $scope.transactions = [];
                      $scope.tableParams.total(0);
                    }
                } else {
                  $scope.transactions = [];
                  $scope.tableParams.total(0);
                }
                $defer.resolve($scope.transactions);
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
        var totalPromise = TransactionService.getIncomeExpenseTotal($scope.searchDto);
        totalPromise.then(
            function (data) {
                $scope.incomeExpenseTotal = data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.add = function() {
        $scope.inserted = angular.copy($scope.newTransaction);
        if ($scope.previousTranDate != undefined) {
            $scope.inserted.onDate = $scope.previousTranDate;
        }
        $scope.transactions.unshift($scope.inserted);
        $scope.inserted.$$edit = true;
        $scope.storeList.push($scope.inserted);
    };
    $scope.addToCurrentMonth = function() {
        var list = $scope.selectedTransactions;
        for (var i = 0; i < list.length; i++) {
            var t = angular.copy(list[i]);
            var onDate = ApplicationService.updateCurrentMonth(t.onDate);
            t.onDate = onDate; 
            $scope.duplicate(t);
        }
    };
    $scope.duplicateAll = function() {
        var list = $scope.selectedTransactions;
        for (var i = 0; i < list.length; i++) {
            $scope.duplicate(list[i]);
        }
    };
    
    $scope.deleteAll = function() {
        var list = $scope.selectedTransactions;
        if (list) {
            var confirmedCallback = function() {
                var promise = TransactionService.deleteAllTransaction(list);
                promise.then(
                    function (data) {
                        $scope.tableParams.reload();
                    },
                    function (reason) {
                        console.log('Failed: ' + reason);
                    }
                );
            };
            var declinedCallback = function() {
                console.info("declined ...");
            };
            var msg = list.length <= 1 ? "this transaction" : "these transactions";
            var itemNames = "";
            for (var i = 0; i < list.length; i++) {
                var t = list[i];
                var text = t.description;
                if (! text) {
                    text = t.category.name;
                }
                itemNames += text;
                if (i < list.length - 1) {
                    itemNames += ", ";
                }
            };
            var data = ApplicationService.getDeleteData(msg, itemNames);
            console.dir(data);
            ApplicationService.confirm(data, confirmedCallback, declinedCallback);
        }
    };
    $scope.showCategory = function(t) {
        if(t.categoryId && $scope.categories.length) {
          var selected = $filter('filter')($scope.categories, {id: t.categoryId});
          return selected.length ? selected[0].name : 'Not set';
        } else {
          return t.category.name || 'Not set';
        }
    };
    $scope.showCategoryFromDto = function(t) {
        if(t.catId && $scope.categories.length) {
          var selected = $filter('filter')($scope.categories, {id: t.catId});
          return selected != undefined ? selected[0].name : 'Not set';
        }
    };
    $scope.showAccount = function(t) {
        if(t.accountId && $scope.accounts.length) {
          var selected = $filter('filter')($scope.accounts, {id: t.accountId});
          console.dir(selected);
          return selected.length ? selected[0].name : 'Not set';
        } else {
          return t.account.name || 'Not set';
        }
    };
    $scope.dateChanged = function(t) {
        console.info("dateChanged");
        $scope.tranChanged(t);
    };
    $scope.duplicate = function(t) {
        if (t != undefined) {
            var t = angular.copy(t);
            t.action.actionIndex = ACTION_INDEX.NEW;
            t.$$selected = false;
            t.id = null;
            t.$$edit = true;
            /*if ($scope.previousTranDate != undefined) {
                t.onDate = $scope.previousTranDate;
            }*/
            if( $scope.storeList.indexOf(t) == -1) {
              $scope.storeList.push(t);  
            }
            console.info("duplicate");
            console.dir(t);
            $scope.transactions.unshift(t);
        }
    };
    $scope.tranChanged = function(t) {
        var index = $scope.storeList.indexOf(t);
        if (index != -1) {
            console.info("tranChanged " + t.description + " index " + index + " " + t.action.actionIndex) ;
            $scope.storeList[index] = t;
        } 
        if (t.action.actionIndex != ACTION_INDEX.NEW) {
            t.action.actionIndex = ACTION_INDEX.UPDATE;
        }
        console.info("tranChanged " + t.description + " " + t.action.actionIndex) ;
        
    };
    $scope.categoryChanged = function(t) {
        console.info("cat changed");
        console.info(t.categoryId);
        if(t.categoryId && $scope.categories.length) {
          var selected = $filter('filter')($scope.categories, {id: t.categoryId});
          t.category = selected[0];
        }
        if (t.category) {
            if (!t.accountId) {
                t.accountId = t.category.defaultAccountId;
            }
            if (! t.description) {
                t.description = t.category.name;
            }
            $scope.tranChanged(t);
        }
    };
    $scope.accountChanged = function(t) {
        console.info(t.accountId);
        if(t.accountId && $scope.accounts.length) {
          var selected = $filter('filter')($scope.accounts, {id: t.accountId});
          t.account = selected[0];
          $scope.tranChanged(t);
        }
    };
    $scope.edit = function(t) {
        t.$$edit = true;
        var index = $scope.storeList.indexOf(t);
        if (index == -1) {
            $scope.storeList.push(t);
        } 
    };
    $scope.cancel = function() {
        // t.$$edit = false;
        // var index = $scope.transactions.indexOf(t);
        // if (t.id) {
        //     $scope.transactions[index] = $scope.original;
        //     $scope.original = undefined;
        // } else {
        //     $scope.transactions.splice(index, 1);
        // }
        /*var list = $scope.selectedTransactions;
        for (var i = 0; i < list.length; i++) {
            list[i].$$selected = false;
            list[i].$$edit = false;
        };*/
        $scope.storeList = [];
        $scope.selectedTransactions = [];
        $scope.tableParams.reload();
    };
    $scope.selectedMonthsChanged = function() {
        console.info("selectedMonthsChanged()");
        console.info($scope.selectedMonths);
    };
    $scope.selectedCategory = function(categoryIds) {
        $scope.selectedCategoryNames = [];
        var length = $scope.searchDto.categoryIds.length;
        for (var i = 0; i < length; i++) {
            var catId = $scope.searchDto.categoryIds[i];
            for (var j = 0; j < $scope.categories.length; j++) {
                var c = $scope.categories[j];
                if (catId == c.id) {
                    $scope.selectedCategoryNames.push(c.name);
                    break;
                }
            }
        };
        console.info("selected");
        console.dir($scope.selectedCategoryNames);
    };
    $scope.advanced = function(t) {
        console.info("advanced");
        var data = {transaction: t, accounts:
            $scope.accounts,
            expCategories: $scope.expCategories,
            incCategories: $scope.incCategories};
        var dlg = dialogs.create('modules/transaction/transaction-dialog.html',
            'DialogController',data,
            'lg');
        dlg.result.then(function(data){
            console.info("save");
            console.dir(data);
        },function(){
            console.info("cancel");
        });
    };
    $scope.thisMonth = function() {
      $scope.setRange($scope.today);
    };
    $scope.prevMonth = function() {
      var prevMonth = ApplicationService.prevMonth($scope.searchDto.fromDate);
      $scope.setRange(prevMonth);
      $scope.search();
    };
    $scope.nextMonth = function() {
      var nextMonth = ApplicationService.nextMonth($scope.searchDto.toDate);
      $scope.setRange(nextMonth);
      $scope.search();
    };
    $scope.setRange = function(date) {
      var fromDate = angular.copy(date);
      fromDate.setDate(1);
      $scope.searchDto.fromDate = fromDate;
      var toDate = angular.copy(date);
      var days = toDate.getDaysInMonth();
      toDate.setDate(days);
      console.info("from: " + fromDate);
      console.info("to: " + toDate);
      $scope.searchDto.toDate = toDate;      
    };
    $scope.select = function(t) {    
        t.$$selected = !t.$$selected;
        if (t.$$selected) {
            $scope.selectedTransactions.push(t);
        } else {
            t.$$selected = false;
            $scope.allSelected = false;
            var index = $scope.selectedTransactions.indexOf(t);
            if (index > -1) {
                $scope.selectedTransactions.splice(index, 1);
            }
        }
        if ($scope.selectedTransactions.length == $scope.transactions.length) {
            $scope.allSelected = true;
        }
    };
    $scope.selectAll = function() {
        $scope.selectedTransactions = [];
        $scope.allSelected = !$scope.allSelected;
        console.info("selectAll " + $scope.allSelected);
        angular.forEach($scope.transactions, function(t) {
            t.$$selected = $scope.allSelected;
            if (t.$$selected) {
                $scope.selectedTransactions.push(t);
            }
        });
    };
}]);
