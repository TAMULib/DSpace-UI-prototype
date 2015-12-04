seedApp.service("Repositories", function(AbstractModel, WsApi) {

	var self;

	var Repositories = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Repositories.data = null;

	Repositories.promise = null;
	
	Repositories.set = function(data) {
		self.unwrap(self, data);
	};

	Repositories.get = function() {
		
		var newRepositoriesPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: "all",
		});

		Repositories.promise = newRepositoriesPromise;
		Repositories.data = new Repositories(newRepositoriesPromise);	
		
		return Repositories.data;
	};

	Repositories.getAffiliation = function() {
		return Repositories.affiliation;
	};

	Repositories.ready = function() {
		return Repositories.promise;
	};

	Repositories.refresh = function() {
		Repositories.promise = null;
		Repositories.get();
	};
	return Repositories;
	
});