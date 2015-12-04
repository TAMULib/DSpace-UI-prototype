seedApp.controller('ThemingController', function ($controller, $scope, $window, ThemeModel) {
	
    angular.extend(this, $controller('AbstractController', {$scope: $scope}));

	$scope.themeValues = {
		"primary": "#500000",
		"secondary": "#3c0000",
		"linkcolor": "#337ab7",
		"basefontsize": "14px"
	}

	themeFieldGlosses = {"linkcolor":"Link Color","basefontsize":"Base Font Size"};
    
//  $scope.currentTheme = Theme.get();
	$scope.saveTheme = function(themeValues) {
		ThemeModel.update(themeValues).then(function() {
			$window.location.reload();
		});
	};

	$scope.getThemeGloss = function(field) {
		return (typeof themeFieldGlosses[field] !== 'undefined' && themeFieldGlosses[field]) ? themeFieldGlosses[field]:field;
	};
});
