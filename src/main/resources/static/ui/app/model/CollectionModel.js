seedApp.service("Collection", function(AbstractModel, WsApi) {

	var self;

	var Collection = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Collection.data = null;

	Collection.promise = null;
	
	Collection.set = function(data) {
		self.unwrap(self, data);
	};

	Collection.get = function(repo, data) {
		var newCollectionPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+"/collection/"+data.collectionId,
				data: data
		});

		Collection.promise = newCollectionPromise;
		Collection.data = new Collection(newCollectionPromise);	
		
		return Collection.data;
	};

	Collection.getOffsetItems = function(repo, data) {
		var newCollectionPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+"/collection/"+data.collectionId,
				data: data
		}).then(function(res) {
			Collection.data.items = JSON.parse(res.body).payload.ObjectNode.items;
		});
	};

	Collection.createItem = function(repo, collectionId, itemName) {

		var data = {
			"collectionId": collectionId,
			"itemName": itemName
		}

		var newCreateItemPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+"/itemcreate",
				data: data
		});

		return newCreateItemPromise

	}

	Collection.getAffiliation = function() {
		return Collection.affiliation;
	};

	Collection.ready = function() {
		return Collection.promise;
	};

	Collection.refresh = function(repo, data) {
		console.log("got here");
		Collection.promise = null;
		Collection.get(repo, data);
	};
	return Collection;
	
});