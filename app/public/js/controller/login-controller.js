angular.module ('titan').controller ('LoginController', function ($scope, settings, registerBrowserWebService)
{
	$scope.google = settings.social.google;

	$scope.$on('event:google-plus-signin-success', function (event, result)
	{
		var email = result.wc.hg;
		var access = result.hg.access_token;
		var validity = result.hg.expires_in;

		registerBrowserWebService ('Google', email, access, validity, function () {
			userIsRegistered ();
		}, function () {
			console.log ('FALHA!');
		});
	});

	$scope.$on('event:google-plus-signin-failure', function (event, result)
	{
		console.log (JSON.stringfy (result));
	});

	var userIsRegistered = function ()
	{
		document.location = 'index.html';
	};
});
