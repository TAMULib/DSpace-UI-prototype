seedApp.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	$locationProvider.html5Mode(true);
	$routeProvider.
		when('/repositories', {
			templateUrl: 'views/repoManagement.html',
			controller: 'RepositoryController',
			restrictTo: 'isAdmin'
		}).
		when('/profile', {
			templateUrl: 'views/myview.html'
		}).
		when('/theming', {
			templateUrl: 'views/theming.html',
			controller: 'ThemingController',
			restrictTo: 'isAdmin'
		}).
		when('/communities', {
			templateUrl: 'views/communities.html',
			controller: 'CommunitiesController'
		}).
		when('/communities/:community', {
			templateUrl: 'views/community.html',
			controller: 'CommunityController'
		}).
		when('/collections', {
			templateUrl: 'views/collections.html',
			controller: 'CollectionsController'
		}).
		when('/collections/:collectionParam', {
			templateUrl: 'views/collection.html',
			controller: 'CollectionController'
		}).
		when('/items/create', {
			templateUrl: 'views/new-item.html',
			controller: 'CreateEditController',
			restrictTo: 'isAdmin'
		}).
		when('/items/:itemParam', {
			templateUrl: 'views/item.html',
			controller: 'ItemController'
		}).
		when('/users', {
			templateUrl: 'views/users.html',
			restrictTo: 'isAdmin'
		}).
		when('/restricted', {
			templateUrl: 'views/restricted.html'
		}).
		otherwise({
			redirectTo: '/',
			templateUrl: 'views/home.html',
			controller: 'HomeController'
		});
}]);