(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TweetAuthorController', TweetAuthorController);

    TweetAuthorController.$inject = ['TweetAuthor'];

    function TweetAuthorController(TweetAuthor) {

        var vm = this;

        vm.tweetAuthors = [];

        loadAll();

        function loadAll() {
            TweetAuthor.query(function(result) {
                vm.tweetAuthors = result;
                vm.searchQuery = null;
            });
        }
    }
})();
