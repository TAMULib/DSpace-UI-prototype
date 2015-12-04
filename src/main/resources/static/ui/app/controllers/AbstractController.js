seedApp.controller('AbstractController', function ($scope, $window, $route, $location, StorageService, RestApi) {

	$scope.storage = StorageService;

	$scope.isAssumed = function() {
		return StorageService.get("assumed");
	};

	$scope.isAssuming = function() {
		return StorageService.get("assuming");
	};

	$scope.isAnonymous = function() {
		return (sessionStorage.role == "ROLE_ANONYMOUS");
	};

	$scope.isUser = function() {
		return (sessionStorage.role == "ROLE_USER");
	};

	$scope.isAnnotator = function() {
		return (sessionStorage.role == "ROLE_ANNOTATOR");
	};

	$scope.isManager = function() {
		return (sessionStorage.role == "ROLE_MANAGER");
	};

	$scope.isAdmin = function() {
		return (sessionStorage.role == "ROLE_ADMIN");
	};

	if($route.current && $scope[$route.current.restrictTo]) {
		if(!$scope[$route.current.restrictTo]()) {
			$location.path( "/restricted" );
		}
	}

	$scope.reportError = function(alert) {
		RestApi.post({
			controller: 'report', 
			method: 'error',
			data: alert
		}).then(function() {
			angular.element("#reportModal").modal('show');
		}, function(response) {
			if(response.data == null || response.data.message != "EXPIRED_JWT") {
				var subject = 'Error Report';
				var body = 'Error Report\n\nchannel: ' + alert.channel +
						   '\ntime: ' + new Date(alert.time) +
						   '\ntype: ' + alert.type + 
						   '\nmessage: ' + alert.message;
	    		$window.location.href = "mailto:"+ coreConfig.alerts.email + 
	    							    "?subject=" + escape(subject) + 
	    								"&body=" + escape(body); 
			}
		}, function() {
			
		});
	};

});
