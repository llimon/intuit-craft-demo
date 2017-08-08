(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TweetAuthorDeleteController',TweetAuthorDeleteController);

    TweetAuthorDeleteController.$inject = ['$uibModalInstance', 'entity', 'TweetAuthor'];

    function TweetAuthorDeleteController($uibModalInstance, entity, TweetAuthor) {
        var vm = this;

        vm.tweetAuthor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TweetAuthor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
