seedApp.service("Items", function(AbstractModel, WsApi) {

	var self;

	var Items = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Items.data = null;

	Items.promise = null;
	
	Items.set = function(data) {
		self.unwrap(self, data);
	};

	Items.get = function(repo, collection) {
		
		var newItemsPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+"/collection/"+collection+"/items",
		});

		Items.promise = newItemsPromise;
		Items.data = new Items(newItemsPromise);	
		
		return Items.data;
	};

	Items.getAffiliation = function() {
		return Items.affiliation;
	};

	Items.ready = function() {
		return Items.promise;
	};

	Items.refresh = function() {
		Items.promise = null;
		Items.get();
	};
	return Items;
	
});