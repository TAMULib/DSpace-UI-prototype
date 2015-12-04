seedApp.controller('AuthenticationController', function ($controller, $location, $scope, $window, AuthServiceApi, StorageService, User, WsApi) {

    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

    $scope.login = function(mock) {
        
        delete sessionStorage.token;
        delete sessionStorage.role;

        User.login();

        User.get();

        var path = '';

        if(typeof page != 'undefined') {
            path = "/" + location.pathname.split("/")[1] + "/" + page;
        }
        else {
            path = location.pathname;
        }

        if(mock) {
            $window.open(appConfig.authService + "/token?referer="+location.origin + path + "&mock=" + mock, "_self");
        }
        else {
            $window.open(appConfig.authService + "/token?referer="+location.origin + path, "_self");
        }

    };

    $scope.logout = function() {

        delete sessionStorage.token;
        sessionStorage.role = "ROLE_ANONYMOUS";

        $(".dropdown").dropdown("toggle");

        $location.path('/');

    };

});
