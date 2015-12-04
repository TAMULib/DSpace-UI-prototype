seedApp.service("Collections", function(AbstractModel, WsApi) {

	var self;

	var Collections = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Collections.data = null;

	Collections.promise = null;
	
	Collections.set = function(data) {
		self.unwrap(self, data);
	};

	Collections.get = function(repo) {
		if(Collections.promise) return Collections.data;
		
		var newCollectionsPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+'/collections',
		});

		Collections.promise = newCollectionsPromise;
		if(Collections.data) {
			newCollectionsPromise.then(function(data) {
				Collections.set(JSON.parse(data.body).payload.String);
			});
		}
		else {
			Collections.data = new Collections(newCollectionsPromise);	
		}

		return Collections.data;
	};

	Collections.getAffiliation = function() {
		return Collections.affiliation;
	};

	Collections.ready = function() {
		return Collections.promise;
	};

	Collections.refresh = function() {
		Collections.promise = null;
		Collections.get();
	};
	return Collections;
	
});