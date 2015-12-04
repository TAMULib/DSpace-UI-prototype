seedApp.service("Communities", function(AbstractModel, WsApi) {

	var self;

	var Communities = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Communities.data = null;

	Communities.promise = null;
	
	Communities.set = function(data) {
		self.unwrap(self, data);
	};

	Communities.get = function(repo) {
		
		var newCommunitiesPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+"/top-communities",
		});

		Communities.promise = newCommunitiesPromise;
		Communities.data = new Communities(newCommunitiesPromise);	
		
		return Communities.data;
	};

	Communities.getAffiliation = function() {
		return Communities.affiliation;
	};

	Communities.ready = function() {
		return Communities.promise;
	};

	Communities.refresh = function() {
		Communities.promise = null;
		Communities.get();
	};
	return Communities;
	
});