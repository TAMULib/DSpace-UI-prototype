seedApp.controller('ItemController', function ($controller, $scope, $sce, $routeParams, Breadcrumb, Sidebar, Item) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));
    
    $scope.item = Item.get("dspace1", $routeParams.itemParam);

    $scope.renderHtml = function(html) {
		return $sce.trustAsHtml(html);
	};

    Item.ready().then(function() {
		Breadcrumb.get("dspace1", $scope.item);
		//Sidebar.set({"title":"Item Options","items":[{'name':'An Item Link','url':'http://google.com'},{'name':'Another Item Link','url':'http://yahoo.com'}]});
		sortMetadata($scope.item.metadata);
    });

    $scope.setShownBitstream = function(bitStream) {
        Item.setShownBitstream(bitStream);
    }

    var sortMetadata = function(metadata) {

    	for(var index in metadata) {
    		var field = metadata[index];
    		
    		switch(field.key) {

    			case "dc.subject":
    				if(typeof $scope.item.subjects == 'undefined') $scope.item.subjects = [];
                    Item.add("subjects", field.value);
    			break;

    			case "dc.description.abstract":
    				Item.add("abstract", field.value);
    			break;

    			case "dc.creator":
    				if(typeof $scope.item.authors == 'undefined') $scope.item.authors = [];
    				Item.add("authors", field.value);
    			break;

    			case "dc.date.created":
                    Item.add("date", field.value);
    			break;

    			
    		}

    	}

    }

});