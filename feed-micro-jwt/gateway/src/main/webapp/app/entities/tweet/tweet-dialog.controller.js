(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TweetDialogController', TweetDialogController);

    TweetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tweet', 'TweetAuthor'];

    function TweetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tweet, TweetAuthor) {
        var vm = this;

        vm.tweet = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tweetauthors = TweetAuthor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tweet.id !== null) {
                Tweet.update(vm.tweet, onSaveSuccess, onSaveError);
            } else {
                Tweet.save(vm.tweet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:tweetUpdate', result);
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
