seedApp.controller('HomeController', function ($controller, $scope) {

    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

    $scope.foo = "bar";

});
