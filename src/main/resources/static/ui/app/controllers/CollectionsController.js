seedApp.controller('CollectionsController', function ($controller, $scope, Collections) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));
    
    $scope.collections = Collections.get("dspace1");

	Collections.ready();
});
