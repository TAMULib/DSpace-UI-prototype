seedApp.controller('RepositoryController', function ($controller, $scope, Repositories) {

    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

    $scope.repositories = Repositories.get();

    Repositories.ready().then(function() {

    	console.log($scope.repositories);

    });

});
