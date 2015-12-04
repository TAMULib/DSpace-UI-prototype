seedApp.controller('SidebarController', function ($controller, $scope, Sidebar) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

	$scope.sidebar = {};
	Sidebar.get().then(function(data) {
		$scope.sidebar.title = data.title;
		$scope.sidebar.list = data.items;
	});
	
});