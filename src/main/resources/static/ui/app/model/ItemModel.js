seedApp.service("Item", function($q, AbstractModel, WsApi) {

	var self;

	var Item = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	Item.data = null;

	Item.promise = null;
	Item.loadedPromise = $q.defer();

	
	Item.set = function(data) {
		self.unwrap(self, data);
	};

	Item.get = function(repo, item) {

		if(!repo) return Item.data;
		
		var newItemPromise = WsApi.fetch({
			endpoint: '/private/queue', 
			controller: 'repositories', 
			method: repo+"/item/"+item
		});

		Item.promise = newItemPromise;

		Item.data = new Item(newItemPromise);	
		
		Item.promise.then(function(data) {
			Item.loadedPromise.notify(data);
			Item.data.shownBitstream = Item.data.bitstreams[0];
		});

		return Item.data;
	};

	Item.getAffiliation = function() {
		return Item.affiliation;
	};

	Item.ready = function() {
		return Item.promise;
	};

	Item.add = function(key, value) {
		if(Item.data[key]) {
			if(! (Item.data[key] instanceof Array)) {
				var oldValue = Item.data[key];
				Item.data[key] = [];
				Item.data[key].push(oldValue);
			}

			Item.data[key].push(value);
		} else {
			Item.data[key] = value;
		}
	}

	Item.setShownBitstream = function(shownBitstream) {
		Item.data.shownBitstream = shownBitstream;
	}

	Item.loaded = function(cb) {
		Item.loadedPromise.promise.then(null,null,function() {
			cb();
		});
		return Item.laodedPromise;
	};

	Item.refresh = function() {
		Item.promise = null;
		Item.get();
	};
	return Item;
	
});