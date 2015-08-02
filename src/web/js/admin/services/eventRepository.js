angular.module('admin.services.events', ['ngResource', 'configuration.authorization']).
factory('eventRepository', ['$resource', '$http', function($resource, $http) {
	return {
		url: '/events',
		getEvents: function() {
			return $resource(this.url, {}).query().$promise;
		},
		getEventsWithStatus: function(status) {
			return $resource(this.url + '/status/' + status, {}).query().$promise;
		},
		getActiveEvents: function(status) {
			return getEventsWithStatus(1);
		},
		getEvent: function(id) {
			return $resource(this.url + '/' + id).get().$promise;
		}
	};
}]);