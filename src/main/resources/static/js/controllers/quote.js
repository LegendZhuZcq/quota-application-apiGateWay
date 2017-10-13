'use strict'

angular.module('app.controllers', [])

.controller('RandomQuoteCtrl', function($scope, QuoteService) {
	QuoteService.random()
		.$promise.then(function(quote) {
			$scope.quote = quote;
		});
})
.controller('AuthorCtrl', function($scope,$stateParams, AuthorService){
	console.log($stateParams.authorName);
	AuthorService.query({author:$stateParams.authorName})
	    .$promise.then(function(quotes){
	    	 console.log(quotes);
	    	 $scope.quotes=quotes;
	    	});
})
.controller('SaveQuoteCtrl', function($scope, $state, QuoteService) {
    
    $scope.saveQuote = function() {
        QuoteService.save(
            $scope.quote,
            function(response) {
                $state.go("quote", {});
            },
            function(err) {
                console.log(err);
            }
        );
    };
});