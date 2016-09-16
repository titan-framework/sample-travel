angular.module ('titan').config (function ($routeProvider, $locationProvider)
{	
	$locationProvider.html5Mode (true);
	
	$routeProvider.when ('/home', {
		templateUrl: 'section/home/dashboard.html',
		controller: 'HomeController'
	});
	
	$routeProvider.when ('/embassy', {
		templateUrl: 'section/embassy/list.html',
		controller: 'EmbassyController'
	});
	
	$routeProvider.otherwise ({ redirectTo: '/home' });
	
});