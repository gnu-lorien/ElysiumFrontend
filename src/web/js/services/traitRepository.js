angular.module('services.traits', ['ngResource']).
factory('traitsRepository', ['$resource', function($resource) {
	return {
		url: '/traits/',
		getVampireTraits: function(traitType, parameters) {
			var resource = $resource(this.url + "vampire/:traitType", {traitType: '@traitType'});
			return resource.query({traitType: traitType}).$promise;
		}
	};
}]);