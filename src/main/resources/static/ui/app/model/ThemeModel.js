seedApp.service("ThemeModel", function($q,AbstractModel, WsApi) {

	var self;

	var Theme = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Theme.data = null;

	Theme.promise = null;
	
	Theme.set = function(data) {
		self.unwrap(self, data);
	};
/*
	Theme.get = function() {
		
		var newThemePromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'theme', 
				method: "/theme/",
		});

		Theme.promise = newThemePromise;
		Theme.data = new Theme(newThemePromise);	
		
		return Theme.data;
	};

	Theme.ready = function() {
		return Theme.promise;
	};

	Theme.refresh = function() {
		Theme.promise = null;
		Theme.get();
	};
*/
	Theme.update = function(themeValues) {
		return $q(function(resolve,reject) {
			WsApi.fetch({
					endpoint: '/private/queue', 
					controller: 'theme', 
					method: 'update',
					data: {
						"themeValues":themeValues
					}
			}).then(function() {
				resolve("Theme Updated");
			},function() {
				reject("Failed to update theme");
			});
		});
	};

	return Theme;
	
});