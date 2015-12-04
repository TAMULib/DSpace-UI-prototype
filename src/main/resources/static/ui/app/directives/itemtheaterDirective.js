seedApp.directive('itemtheater', function (Item, Repositories) {
	return {
		templateUrl: 'views/itemTheater.html',
		restrict: 'E',
		replace: true,
		transclude: true,
		link: function ($scope, element, attr) {

			$scope.itemLoaded = false;

			Item.loaded(function() {
				$scope.itemLoaded = true;
				$scope.item = Item.get();
				
				console.log($scope.item)
				
				$scope.$on("$routeChangeStart", function() {
					$scope.itemLoaded = false;
				});
			});
	    }
	};
});