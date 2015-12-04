seedApp.controller('CollectionController', function ($controller, $scope, $routeParams, Collection, Breadcrumb, Sidebar, Items) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

    $scope.offset = $routeParams.offset;
    if($scope.offset == null) $scope.offset = 0;
    
    var data = {
    		collectionId : $routeParams.collectionParam,
    		offset : $scope.offset,
    };
    
    $scope.collection = Collection.get("dspace1", data);

	Collection.ready().then(function() {
		Breadcrumb.get("dspace1", $scope.collection);
		Sidebar.set({"title":"Collection Options","items":[{'name':'A Collection Link','url':'http://google.com'},{'name':'Another Collection Link','url':'http://yahoo.com'}]});
	});

	$scope.loadResults=function(newOffset){
		
		if(newOffset < 0) newOffset = 0;
		if(newOffset > $scope.collection.numberItems) newOffset = $scope.collection.numberItems - 100;
		
		$scope.offset = newOffset;
		data.offset = $scope.offset;
		Collection.getOffsetItems("dspace1", data);
	};
});