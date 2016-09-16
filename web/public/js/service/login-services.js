angular.module ('titan').factory ('registerBrowserWebService', ['settings', '$http', '$localStorage', function (settings, $http, $localStorage)
{
	return function (driver, email, access, validity, success, error)
	{
		var appID = settings.api.app;
		var appPK = settings.api.pk;
		
		var timestamp = Math.floor (Date.now () / 1000);
		
		var signature = CryptoJS.HmacSHA1 (timestamp + appID, appPK);
		
		var secret = '' + CryptoJS.MD5 (appPK + timestamp);
		
		secret = secret.substr (0, 16);
		
		var token = blowfish.encrypt (access, secret, { cipherMode: 0, outputType: 0 });
		
		$http ({
			method: 'POST',
			url: settings.api.uri + '/social',
			headers : {
				'x-embrapa-auth-timestamp': timestamp,
				'x-embrapa-auth-application-id': appID,
				"x-embrapa-auth-application-signature": signature
			},
			data: {
				driver: driver,
				email: email,
				token: token,
				validity: validity
			}
		})
		.success (function (data) 
		{	
			var userPK = blowfish.decrypt (data.pk, secret, { cipherMode: 0, outputType: 0 });
			
			$localStorage.userID = email;
			$localStorage.userPK = userPK;
			
			success ();
		})
		.error (function (message) {
			error (message);
		});
	}
}]);

angular.module ('titan').factory ('getUserProfileWebService', ['ws', function (ws)
{
	return function (success, error)
	{
		ws.get ('/auth', success, error);
	}
}]);