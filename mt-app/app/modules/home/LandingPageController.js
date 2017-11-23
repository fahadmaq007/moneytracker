'use strict';

angular.module('mt-app')
    .controller('LandingPageController', ['dialogs', '$rootScope', '$scope', '$state', '$location', 'UserService',
    function (dialogs, $rootScope, $scope, $state, $location, UserService) {
    console.info("LandingPageController");
    $scope.myInterval = 5000;
    $scope.featuresInterval = 10000;
  $scope.slides = [
    { // Finance.png
      image: 'common/images/Carousel/Money.jpg',
      logo: 'common/images/128/Money.png',
      textColor: '#FFF',
      caption: 'Track your Money',
      heading: 'Money… Money…. Where are you?',
      description: 'You work hard, spend, invest, try to save, but it never seems to add up. Sounds familiar?.... No Problem! Track your income and expenses here.',
    },
    {
      image: 'common/images/Carousel/PlanBudget.jpg',
      textColor: '#FFF',
      caption: 'Plan your Budget'
    },
    { //fin-report.png
      image: 'common/images/Carousel/Reminder.jpg',
      textColor: '#252530',
      caption: 'Get Reminders'
    },
    {
      image: 'common/images/Carousel/Statement.jpg',
      textColor: '#FFF',
      caption: 'Import Statements (Bank & Personal)'
    },
    {
      image: 'common/images/Carousel/OneView.jpg',
      textColor: '#252530',
      caption: 'See all your Accounts in a singal View'
    },
    {
      image: 'common/images/Carousel/Category.jpg',
      textColor: '#FFF',
      caption: 'Group your own Category Hierarchy'
    },
    {
      image: 'common/images/Carousel/Report.jpg',
      textColor: '#FFF',
      caption: 'Analyze Visual Reports'
    }
  ];

  $scope.featuresInterval = 10000;
  var bulletMoney = 'common/images/32/Money.png';

  $scope.trackMyMoneyHeading = 'Track Your Money';  
  $scope.trackMyMoneyFeatures = [
    { 
      bullet: bulletMoney,
      textColor: '#FFF',      
      description: 'Tracking money at your finger tips! Just enter your daily transactions in an intuitive user interface within seconds.',
    },
    {
      bullet: bulletMoney,
      textColor: '#FFF',      
      description: 'Hassle free editing. Don\'t navigate around pages, just do it inline.',
    },
    { 
      bullet: bulletMoney,
      textColor: '#FFF',
      description: 'Want to repeat the transactions? Just select the required ones & click on Duplicate button. It\'s that simple!'
    },
    {
      bullet: bulletMoney,
      textColor: '#FFF',      
      description: 'Quickly add previous month\'s transactions to Current Month.',
    },
    {
      bullet: bulletMoney,
      textColor: '#FFF',
      description: 'Open in Advanced Layout? Click on the Expand icon after the Description. ' +
       'Advanced features will split the transaction across months, capture recurring & transfer transactions.'
    }
  ];

  var bulletExcel = 'common/images/32/Money.png';
  $scope.statementFeatures = [
    { 
      bullet: bulletExcel,
      textColor: '#FFF',      
      description: 'Lazy enough to enter the transactions daily? No Problem! Import your bank & personally maintained statements (Spreadsheet) at the end of the month...',
    },
    {
      bullet: bulletExcel,
      textColor: '#FFF',      
      description: 'Manage your personal Spreadsheets in a common place like Google Drive (Sample is shared above, put it in your Drive). All you would manage here on a daily basis is how much you spent & on what.',
    },
    { 
      bullet: bulletExcel,
      textColor: '#FFF',
      description: 'Download your bank statements every month end & upload here. It\'s that Simple.'
    },
    {
      bullet: bulletExcel,
      textColor: '#FFF',      
      description: 'Create your Statements with unlimited number of Data Maps so that when you upload the Spreadsheet the system auto populates the Category & Account.',
    }
  ];

  $scope.budgetFeatures = [
    { 
      bullet: bulletExcel,
      textColor: '#FFF',      
      description: 'No idea how to meet your budgeted amount? Track your budget and see where you spend. Categorise, allocate and plan here.',
    },
    {
      bullet: bulletExcel,
      textColor: '#FFF',      
      description: 'Create your monthly budget according to your own choice and let the system show you how the progress is been.',
    },
    { 
      bullet: bulletExcel,
      textColor: '#FFF',
      description: 'Create and Manage as many as budgets you want.'
    },
    {
      bullet: bulletExcel,
      textColor: '#FFF',      
      description: 'Compare your current budget and the spendings with previous month\'s.',
    }
  ];
  $scope.onTrackMoneySlideChanged = function (slide, direction) {
    console.log('onSlideChanged:', direction, slide);
    $scope.trackMoneyCurrentSlide = slide;
  };
}]);
