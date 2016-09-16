angular.module ('titan').factory ('getEmbassiesWebService', ['ws', function (ws)
{
	return function (success, error)
	{
		ws.get ('/embassy/list/0', success, error);
	}
}]);