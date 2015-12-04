seedApp.controller('CommunityController', function ($controller, $scope, $routeParams, $sce, Breadcrumb, Community, Sidebar) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));
    
    $scope.community = Community.get("dspace1", $routeParams.community);

	$scope.renderHtml = function(html) {
		return $sce.trustAsHtml(html);
	};

	Community.ready().then(function() {
		Breadcrumb.get("dspace1", $scope.community);
		Sidebar.set({"title":"Community Options","items":[{'name':'tesst','url':'url'}]});
	});
});