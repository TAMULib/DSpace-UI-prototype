seedApp.service("IRService",function(WsApi) {

	var IR = this;
/*
	IR.restBase = appConfig.webService+"/rest/repositories/";
	IR.getCollections = function(irType) {
		var collectionsPromise = RestApi.get(IR.restBase+"get-collections/");
		return collectionsPromise;
	};
*/
	IR.getCollections = function(irType) {
//		var collectionsPromise = RestApi.get(IR.restBase+"get-collections/");

		var collectionsPromise = WsApi.fetch({
				'endpoint': '/private/queue', 
				'controller': 'repositories', 
				'method': 'get-collections',
		});
		return collectionsPromise;
	};
});
