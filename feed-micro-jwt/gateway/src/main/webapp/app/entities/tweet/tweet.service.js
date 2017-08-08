(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Tweet', Tweet);

    Tweet.$inject = ['$resource', 'DateUtils'];

    function Tweet ($resource, DateUtils) {
        var resourceUrl =  'tweetfeed/' + 'api/tweets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
