(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('TweetAuthor', TweetAuthor);

    TweetAuthor.$inject = ['$resource', 'DateUtils'];

    function TweetAuthor ($resource, DateUtils) {
        var resourceUrl =  'tweetfeed/' + 'api/tweet-authors/:id';

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
