angular.module ('titan').factory ('ws', ['settings', '$http', '$localStorage', function (settings, $http, $localStorage)
{
	var ws = {
		get: function (endpoint, success, error)
		{
			var appID = settings.api.app;
			var appPK = settings.api.pk;
			
			var usrID = $localStorage.userID;
			var usrPK = $localStorage.userPK;
			
			var timestamp = Math.floor (Date.now () / 1000);
			
			var appSignature = CryptoJS.HmacSHA1 (timestamp + appID, appPK);
			var usrSignature = CryptoJS.HmacSHA1 (timestamp + usrID, usrPK);
			
			$http ({
				method: 'GET',
				url: settings.api.uri + endpoint,
				headers : {
					'x-embrapa-auth-timestamp': timestamp,
					'x-embrapa-auth-application-id': appID,
					'x-embrapa-auth-application-signature': appSignature,
					'x-embrapa-auth-user-id': usrID,
					'x-embrapa-auth-user-signature': usrSignature
				}
			})
			.success (function (data) 
			{	
				success (data);
			})
			.error (function (message) {
				error (message);
			});
		}
	};
	
	return ws;
}]);