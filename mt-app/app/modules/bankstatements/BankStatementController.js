'use strict';

angular.module('mt-app')
    .controller('BankStatementController', ['$timeout','$rootScope', '$filter', '$scope', 'hotkeys', '$window', 
        'BankStatementService','DomainService', 'ngTableParams', 'TransactionService', 'ACTION_INDEX',
    function ($timeout, $rootScope, $filter, $scope, $hotkeys, $window, BankStatementService, DomainService, ngTableParams, TransactionService, ACTION_INDEX) {
    console.info("BankStatementController ");
    $scope.newBankStatement = { 'name': '', 'dateFormat': 'dd/MM/yy', 
    'action': {'actionIndex' : 1} };
    $scope.newDataField = { 'propertyName': 'categoryId', 'dataAsString': '', 
    'action': {'actionIndex' : 1} };
    $scope.newDataMap = { 'searchField': 'description', 'searchText': '', 
    'action': {'actionIndex' : 1} };
    $scope.newColumnMap = { 'propertyName': '', 
    'action': {'actionIndex' : 1} };
    $scope.transactions = [];
    $scope.importedList = [];
    $scope.imported = false;
    $scope.selectedTransactions = [];

    $scope.allSelected = false;
    $scope.chunk = function(arr, partition) {
      var newArr = [];

        if (arr == undefined || arr.length == 0) {
            return newArr;
        }
      
      var count = arr.length / partition;
      for (var i = 0; i < partition; i++) {
        var slicedData = arr.slice(i * count, count + (i * count));
        newArr.push(slicedData);
      }
      console.info("chunk");
      console.dir(newArr);
      return newArr;
    };
    $scope.init = function () {
        BankStatementService.init($scope);
        $scope.$show = 'import';
        $scope.searchDto = {
            'page': { 'pageNumber': 1, 'pageSize': 10, 'totalRecords' : 0}
        };
        console.info($scope.searchDto.fromDate);
        $scope.importTableParams = new ngTableParams({
            page: $scope.searchDto.page.pageNumber,            // show first page
            count: $scope.searchDto.page.pageSize,           // count per page
            sorting: {
                onDate: 'asc'     // initial sorting
            }
        }, {
            total: $scope.importedList.length,
            getData: function($defer, params) { 
                var page = params.page();              
                var count = params.count();

                var filteredData = $scope.importedList;
                if (filteredData.length <= 0) {
                    return;
                }
                $scope.importTableParams.total($scope.importedList.length);
                var orderedData = params.sorting() ?
                                    $filter('orderBy')(filteredData, params.orderBy()) :
                                    filteredData;

                var slicedData = orderedData.slice((page - 1) * count, page * count);
                console.info("slicedData " + ((page - 1) * count) + " " + page  * count);                    
                console.dir(slicedData);
                $scope.transactions = slicedData;
                $scope.allSelected = false;
                $defer.resolve($scope.transactions);
            }
        });
        $scope.importTableParams.settings().$scope = $scope;
        
        $scope.loadCategories();
        $scope.loadAccounts();
        
        $scope.loadExcelColumns();
        $scope.loadTransactionColumns();

        $scope.loadBankStatements();
        $hotkeys.add({
            combo: 'alt+a',
            description: 'Add Transaction',
            callback: function() {
              $scope.add();
            }
          });
    };

    $scope.loadTransactionColumns = function() {
      var promise = TransactionService.listTransactionColumns();
        promise.then(
            function (data) {
                console.dir(data);
                $scope.transactionColumns = data;                        
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.loadBankStatements =function() {
        var promise = BankStatementService.listBankStatements();
        promise.then(
            function (data) {
                console.dir(data);
                if (data != undefined || data != null) {
                    $scope.bankStatements = data;
                    if (data.length > 0) {
                        $scope.selectedStatement = data[0];
                    } 
                }                        
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    }; 
    $scope.loadCategories =function() {
        var promise = DomainService.listCategories($scope.domainSearchDto);
        promise.then(
            function (data) {
                $scope.categories = data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    }; 
    $scope.loadAccounts =function() {
        var promise = DomainService.listAccounts($scope.domainSearchDto);
        promise.then(
            function (data) {
                $scope.accounts = data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    }; 
    $scope.loadExcelColumns =function() {
        var promise = BankStatementService.listExcelColumns();
        promise.then(
            function (data) {
                $scope.excelColumns = data;
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    }; 
    $scope.search = function() {
        $scope.importTableParams.page(0);
        $scope.importTableParams.reload();
    };
    
    $scope.clearCategories = function() {
        $scope.searchDto.categoryIds = [];
        $scope.search();
    };
    $scope.clearAccounts = function() {
        $scope.searchDto.accountIds = [];
        $scope.search();
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
    $scope.setFiles = function(element) {
        $scope.$apply(function($scope) {
            var files = element.files;
            console.dir(files);
            $scope.selectedFile = files[0];
        });
    }; 
    $scope.importPreview = function() {
        console.info("importPreview");
        var promise = BankStatementService.importBankStatement(
            $scope.selectedStatement, $scope.selectedFile);
        promise.then(
            function (data) {
                if (data) {
                    console.info("data");
                    $scope.importedList = data; 
                    $scope.imported = false; 
                    $scope.allSelected = false;  
                } 
                console.dir($scope.transactions);
                $scope.importTableParams.reload();
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
        if ($scope.transactions == undefined) {
            $scope.transactions = [];
        }
        $scope.transactions.unshift($scope.inserted);
        $scope.inserted.$$edit = true;
        //angular.element("#onDate").focus();
    }; 
    $scope.duplicate = function(t) {
        console.info(t);
        if (t != undefined) {
            var t = angular.copy(t);
            t.id = null;
            t.action.actionIndex = 1;
            t.$$edit = true;
            if ($scope.previousTranDate != undefined) {
                t.onDate = $scope.previousTranDate;    
            }
            $scope.transactions.unshift(t);
        }        
    }; 
    $scope.delete = function(t) {
        if (t != undefined) {
            console.info("deleting ..." + t);
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
    $scope.$watch('selectedStatement', function() {
        $scope.requestDataMapChunk();
    });
    $scope.requestDataMapChunk = function() {
        if ($scope.selectedStatement != undefined) {
            $scope.chunckedDataMaps = $scope.chunk($scope.selectedStatement.dataMaps, 2);     
        }    
    };
    $scope.statementChanged = function(statement) {
        console.info("statement changed");
        console.dir(statement);
        var promise = BankStatementService.getBankStatementDetails(statement);
        promise.then(
            function (data) {
                if (data != undefined) {
                    $scope.selectedStatement = data;                    
                }
                console.dir(data);
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.categoryChanged = function(t) {
        console.info("cat changed");
        console.info(t.categoryId);
        if(t.categoryId && $scope.categories.length) {
          var selected = $filter('filter')($scope.categories, {id: t.categoryId});
          t.category = selected[0];
        } 
        if (t.category) {
            t.accountId = t.category.defaultAccountId;
            if (! t.description) {
                t.description = t.category.name;    
            }            
        }
    };
    $scope.accountChanged = function(statement) {
        console.info(statement.bankAccountId);
        if(statement.bankAccountId && $scope.accounts.length) {
          var selected = $filter('filter')($scope.accounts, {id: statement.bankAccountId});
          statement.bankAccount = selected[0];
        } 
    };
    $scope.statementDetailsChanged = function(s) {
        if (s.action.actionIndex != ACTION_INDEX.NEW) {
            s.action.actionIndex = ACTION_INDEX.UPDATE;
        }
    };
    $scope.save = function() {
        console.info("saveBankStatement");
        console.dir($scope.bankStatement);
        var promise = BankStatementService.saveBankStatementDetail($scope.selectedStatement);
        promise.then(
            function (data) {
               $scope.selectedStatement = data;                         
            },
            function (reason) {
                console.log('Failed: ' + reason);
            }
        );
    };
    $scope.edit = function(obj) {
        obj.$$edit = true;
        obj.$$selected = true;
        $scope.doAfterSelection(obj);        
    };
    $scope.cancel = function(obj) {
        obj.$$edit = false;
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
    $scope.tranSelectionChanged = function(t) {
        console.dir(t);
        if (t.$$selected) {
            console.info("is selected");
        }
    };   
    $scope.tranSelectionAll = function(selectAll) {
        for (var i = 0; i < $scope.transactions.length; i++) {
            var t = $scope.transactions[i];
            t.$$selected = selectAll;
        };
    };
    $scope.columnMapChanged = function(columnMap) {
        var selectedColJson = columnMap.$$selectedCol;
        if (selectedColJson) {
            var selectedCol = angular.fromJson(selectedColJson);
            console.info("colChanged");
            console.dir(selectedCol);
            columnMap.propertyName = selectedCol.propName;
            columnMap.dataType = selectedCol.dataType;
        }
    };
    $scope.addColumnMap = function(bankStatement) {
        $scope.insertedColumnMap = angular.copy($scope.newColumnMap);
        if (bankStatement.columnMaps == undefined) {
            bankStatement.columnMaps = [];
        }
        $scope.insertedColumnMap.bankStatementId = bankStatement.id;
        bankStatement.columnMaps.unshift($scope.insertedColumnMap);
        $scope.insertedColumnMap.$$edit = true;
    };
    $scope.removeColumnMap = function(colMap) {
        var index = selectedStatement.columnMaps.indexOf(colMap); 

    };
    $scope.addDataMap = function(bankStatement) {
        console.info("addDataMap");
        $scope.insertedDataMap = angular.copy($scope.newDataMap);
        if (bankStatement.dataMaps == undefined) {
            bankStatement.dataMaps = [];
        }
        $scope.insertedDataMap.bankStatementId = bankStatement.id;
        bankStatement.dataMaps.unshift($scope.insertedDataMap);
        $scope.insertedDataMap.$$edit = true;
        var dataField = $scope.addDataField($scope.insertedDataMap);
        dataField.propertyName = 'accountId';
        dataField.dataAsString = bankStatement.bankAccountId;
        console.dir(dataField);
        $scope.dataFieldChanged(dataField);
        $scope.requestDataMapChunk();
    };
    $scope.addDataField = function(dataMap) {
        $scope.insertedDataField = angular.copy($scope.newDataField);
        if (dataMap.dataFields == undefined) {
            dataMap.dataFields = [];
        }
        $scope.insertedDataField.dataMapId = dataMap.id;
        dataMap.dataFields.unshift($scope.insertedDataField);
        $scope.insertedDataField.$$edit = true;
        return $scope.insertedDataField;
    }; 
    $scope.editDataField = function(dataField) {
        dataField.$$edit = true;        
    };
    $scope.cancelDataField = function(dataField) {
        dataField.$$edit = false;
    }; 
    $scope.dataFieldChanged = function(dataField) {
        console.info("cat changed");
        var propertyName = dataField.propertyName;
        var selected = $filter('filter')($scope.transactionColumns, {propName: propertyName});
        console.dir(selected);
        if (selected != null && selected.length > 0) {
            dataField.dataType = selected[0].dataType;
        }
        if (dataField.action.actionIndex != ACTION_INDEX.NEW) {
            dataField.action.actionIndex = ACTION_INDEX.UPDATE;    
        }        
        console.dir(dataField);
    };
    $scope.initDataFieldCategory = function(dataField) {
        console.info("initDataFieldCategory");
        /*var catId = dataField.dataAsString;
        
        if (catId != undefined) {
            dataField.$data = catId;    
        }*/
        console.dir(dataField);
    };
    $scope.selectAll = function() {
        $scope.selectedTransactions = [];
        $scope.allSelected = !$scope.allSelected;
        var selected = $scope.allSelected;
        console.info("selectAll " + $scope.allSelected);
        for (var i = 0; i < $scope.transactions.length; i++) {
            var t = $scope.transactions[i];
            t.$$selected = selected;
            $scope.doAfterSelection(t);
        }
    };
    $scope.select = function(t) {    
        t.$$selected = !t.$$selected;
        $scope.doAfterSelection(t);
    };
    $scope.doAfterSelection = function(t) {
        var index = $scope.selectedTransactions.indexOf(t);
        console.info(t.description + " " + t.$$selected + " " + index);
        
        if (t.$$selected) {
            if (index == -1) {  
                if (! TransactionService.isValidTransaction(t)) {                    
                    $timeout(function(){
                      t.$$edit = true; 
                    });
                }
                $scope.selectedTransactions.push(t);         
            }
        } else {
            t.$$edit = false;
            if (index > -1) {
                $scope.selectedTransactions.splice(index, 1);
            }
        }
        $scope.allSelected = $scope.selectedTransactions.length >= $scope.transactions.length;
        console.dir($scope.selectedTransactions);
    };
    $scope.importTransactions = function() {
        var selectedTrans = $scope.selectedTransactions;
        console.dir(selectedTrans);
        if (selectedTrans.length > 0) {
            var indexes = [];
            for (var i = 0; i < selectedTrans.length; i++) {
                var t = selectedTrans[i];
                t.action.actionIndex = ACTION_INDEX.NEW;
                var index = $scope.transactions.indexOf(t);
                indexes.push(index);
            };
            var promise = TransactionService.storeTransactions(selectedTrans);
            promise.then(
                function (data) {
                    if (data && data.length > 0) {
                        var imported = [];
                        for (var i = 0; i < data.length; i++) {
                            var t = data[i];
                            var index = indexes[i];  
                            if (t.id) {
                                imported.push(t);
                            }
                            $scope.transactions[index] = t;
                        };
                        if (imported.length > 0) {
                            BankStatementService.storeImportedTransactions(imported);         
                        }
                        $scope.selectedTransactions = [];
                        $scope.imported = true; 
                    }
                },
                function (reason) {
                    console.log('Failed: ' + reason);
                }
            );

        }
    };
    $scope.getTransactionColumnDisplayName = function(propertyName) {
        var selected = $filter('filter')($scope.transactionColumns, {propName: propertyName});
        if (selected != null && selected.length > 0) {
            return selected[0].displayName;
        }
    };
    $scope.getCategoryDisplayName = function(catId) {
        var selected = $filter('filter')($scope.categories, {id: catId});
        if (selected != null && selected.length > 0) {
            return selected[0].displayName;
        }
    };
    $scope.getAccountDisplayName = function(acctId) {
        var selected = $filter('filter')($scope.accounts, {id: acctId});
        if (selected != null && selected.length > 0) {
            return selected[0].name;
        }
    };
    $scope.createNewBankStatement = function(name) {
        console.info(name + " " + $scope.newBankStatementName);
        var bs = angular.copy($scope.newBankStatement);
        bs.name = name;
        $scope.selectedStatement = bs;
    };
}]);
