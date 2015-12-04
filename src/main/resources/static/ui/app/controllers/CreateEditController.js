seedApp.controller('CreateEditController', function ($controller, $scope, $routeParams, Breadcrumb, Collections, Collection) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));
    
    $scope.collections = Collections.get("dspace1");

	Collections.ready().then(function() {

		$scope.create = function() {

			Collection.createItem("dspace1", $scope.selectedCollection, $scope.newItemName)

		}

	});
    

});