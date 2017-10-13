'use strict'

angular.module('app.services', [])

.factory('QuoteService', function($resource) {
	return $resource('/api/quote/:id', {id:'@_id'}, {
		random: {
			method: 'GET',
			url: '/api/quote/random'
		}
	});
})
.factory('AuthorService', function($resource){
	console.log()
	return $resource('/api/quoteByAuthor',{author:'@author'},{
		query:{
			method: 'GET'
		}
	});
});