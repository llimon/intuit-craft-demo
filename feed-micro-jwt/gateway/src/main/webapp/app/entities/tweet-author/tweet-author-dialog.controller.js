(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TweetAuthorDialogController', TweetAuthorDialogController);

    TweetAuthorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TweetAuthor', 'Tweet'];

    function TweetAuthorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TweetAuthor, Tweet) {
        var vm = this;

        vm.tweetAuthor = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tweets = Tweet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tweetAuthor.id !== null) {
                TweetAuthor.update(vm.tweetAuthor, onSaveSuccess, onSaveError);
            } else {
                TweetAuthor.save(vm.tweetAuthor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:tweetAuthorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
