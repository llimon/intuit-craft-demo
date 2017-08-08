(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TweetDetailController', TweetDetailController);

    TweetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tweet', 'TweetAuthor'];

    function TweetDetailController($scope, $rootScope, $stateParams, previousState, entity, Tweet, TweetAuthor) {
        var vm = this;

        vm.tweet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:tweetUpdate', function(event, result) {
            vm.tweet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
