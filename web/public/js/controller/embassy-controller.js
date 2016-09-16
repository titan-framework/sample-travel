angular.module ('titan').controller ('EmbassyController', function ($scope, $http, $location, getEmbassiesWebService)
{
	$scope.filter = '';
	
	$scope.embassies = [];
	
	getEmbassiesWebService (function (data) {
		$scope.embassies = data;
	}, function (message) {
		console.log (JSON.stringify (message));
		
		document.location = 'login.html';
	});
});