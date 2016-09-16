angular.module ('titan').controller ('ProfileController', function ($scope, getUserProfileWebService)
{
	getUserProfileWebService (function (data) {
		console.log (JSON.stringify (data));
		$scope.user = data;
	}, function (result) {
		console.log (JSON.stringify (result));
	});
});