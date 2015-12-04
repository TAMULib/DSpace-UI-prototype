seedApp.service("Sidebar", function($q,AbstractModel) {


	var self;

	var Sidebar = function() {
		self = this;
		angular.extend(self, AbstractModel);	
	};

	var init = $q.defer();

	Sidebar.data = null;
	Sidebar.dataReady = init;
	
	Sidebar.set = function(data) {
		Sidebar.data = data;
		Sidebar.data.off = false;
		Sidebar.dataReady.resolve(Sidebar.data);

	};

	Sidebar.get = function() {
		return Sidebar.dataReady.promise;
	};

	return Sidebar;
	
});