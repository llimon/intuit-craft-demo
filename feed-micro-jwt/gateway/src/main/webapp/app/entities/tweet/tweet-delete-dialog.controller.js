(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TweetDeleteController',TweetDeleteController);

    TweetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tweet'];

    function TweetDeleteController($uibModalInstance, entity, Tweet) {
        var vm = this;

        vm.tweet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tweet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
