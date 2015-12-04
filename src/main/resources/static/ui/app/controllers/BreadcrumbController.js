seedApp.controller('BreadcrumbController', function ($controller, $scope, Breadcrumb) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

	Breadcrumb.loaded(function() {
		$scope.breadcrumbs = Breadcrumb.get();

		$scope.$on("$routeChangeStart", function() {
			Breadcrumb.clear(); 
		});

		$scope.$on("$routeChangeSuccess", function() {
			$scope.breadcrumbs = Breadcrumb.get(); 
		});
	});

});