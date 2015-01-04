angular.module('user.character.manage.services', ['ngResource', 'services.redirection', 'services.csrfResource', 'services.troupes', 'services.characters']);

angular.module('user.character.manage.controllers', ['user.character.manage.services', 'sources.settings', 'sources.clans']).
controller('manageCharacter', ['$scope', '$rootScope', 'redirect', 'characterRepository', 'clanSource', 'bloodlineSource', function($scope, $rootScope, redirect, characterRepository, clanSource, bloodlineSource) {
	$scope.requests = [];
	$scope.clans = clanSource.get();
	$scope.clan = $scope.clans[$scope.character.clan];
	
	$scope.bloodlines = [];
	if($scope.clan) {
		$scope.bloodlines = $scope.clan.bloodlines
		$scope.bloodline = bloodlineSource.get()[$scope.character.bloodline];
	}
	
	$scope.clanChange = function() {
		if($scope.clan) {
			$scope.requests.push({"trait": 0, "value": $scope.clan.id});
			$scope.bloodlines = $scope.clan.bloodlines
		} else {
			$scope.bloodlines = [];
		}
	}
	$scope.bloodlineChange = function() {
		if($scope.bloodline) {
			$scope.requests.push({"trait": 1, "value": $scope.bloodline.id});
		}
	}
	$scope.submit = function(csrfHeader, csrfToken) {
		characterRepository.addRequestsToCharacter($scope.character.id, $scope.requests, csrfHeader, csrfToken).
			success(function(data, status, headers, config) {redirect.toUrl('/user/page/characters')}).
			error(function(data, status, headers, config) {console.log("addRequestsToCharacter failed")});
	};
	
}]);

angular.module('user.character.manage.directives', ['user.character.manage.services']).
directive('manageCharacter', ['characterRepository', function(characterRepository) {
	return {
		restrict: 'E',
		scope: {
			character: '=',
			csrf: '='
		},
		templateUrl: '/js/user/character/manage.html'
	};
}]);

angular.module('user.character.manage.filters', ['filters.setting']);

angular.module('user.character.manage', ['user.character.manage.filters', 'user.character.manage.controllers', 'user.character.manage.directives', 'user.character.manage.services']);