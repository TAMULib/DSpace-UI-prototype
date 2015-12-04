seedApp.service("Community", function(AbstractModel, WsApi) {

	var self;

	var Communites = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Communites.data = null;

	Communites.promise = null;
	
	Communites.set = function(data) {
		self.unwrap(self, data);
	};

	Communites.get = function(repo, community) {
		
		var newCommunitesPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+"/community/"+community,
		});

		Communites.promise = newCommunitesPromise;
		Communites.data = new Communites(newCommunitesPromise);	
		
		return Communites.data;
	};

	Communites.getAffiliation = function() {
		return Communites.affiliation;
	};

	Communites.ready = function() {
		return Communites.promise;
	};

	Communites.refresh = function() {
		Communites.promise = null;
		Communites.get();
	};
	return Communites;
	
});