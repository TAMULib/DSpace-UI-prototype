seedApp.service("Breadcrumb", function($q, AbstractModel, WsApi) {

	var self;

	var Breadcrumb = function(futureData) {
		self = this;
		angular.extend(self, AbstractModel);	
		self.unwrap(self, futureData);		
	};

	var init = $q.defer();

	Breadcrumb.data = null;
	Breadcrumb.loadedPromise = $q.defer();
	Breadcrumb.promise = init.promise;
	
	Breadcrumb.set = function(data) {
		self.unwrap(self, data);
	};

	Breadcrumb.get = function(repo, start) {

		if(!repo) return Breadcrumb.data;

		var type;
		switch(start.type) {
		    case "collection":
		        type = "collections";
		        break;
		    case "community":
		        type = "communities";
		        break;
		    default:
		        type = "items";
		}

		var newBreadcrumbPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'repositories', 
				method: repo+"/breadcrumb/"+type+"/"+start.id,
		});

		newBreadcrumbPromise.then(function(data) {
			Breadcrumb.setCurrent(start.name);
			init.resolve(data);
			Breadcrumb.loadedPromise.notify(data);
		});		

		Breadcrumb.data = new Breadcrumb(newBreadcrumbPromise);	

		return Breadcrumb.data;
	};

	Breadcrumb.getCurrent = function() {
		return Breadcrumb.data.current;
	}

	Breadcrumb.setCurrent = function(current) {
		Breadcrumb.data.current = current;
	} 

	Breadcrumb.ready = function() {
		return Breadcrumb.promise;
	};

	Breadcrumb.loaded = function(cb) {
		Breadcrumb.loadedPromise.promise.then(null,null,function() {
			cb();
		});
		return Breadcrumb.laodedPromise;
	};

	Breadcrumb.clear = function(repo, start) {
		Breadcrumb.data = null;
		Breadcrumb.promise = init.promise;
	};
	return Breadcrumb;
	
});