(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TweetAuthorDetailController', TweetAuthorDetailController);

    TweetAuthorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TweetAuthor', 'Tweet'];

    function TweetAuthorDetailController($scope, $rootScope, $stateParams, previousState, entity, TweetAuthor, Tweet) {
        var vm = this;

        vm.tweetAuthor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:tweetAuthorUpdate', function(event, result) {
            vm.tweetAuthor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
