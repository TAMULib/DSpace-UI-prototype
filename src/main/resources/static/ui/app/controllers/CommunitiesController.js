seedApp.controller('CommunitiesController', function ($controller, $scope, Communities) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));
    
    $scope.communities = Communities.get("dspace1");

	Communities.ready();
});