(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tweet', {
            parent: 'entity',
            url: '/tweet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tweets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tweet/tweets.html',
                    controller: 'TweetController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('tweet-detail', {
            parent: 'tweet',
            url: '/tweet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tweet'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tweet/tweet-detail.html',
                    controller: 'TweetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tweet', function($stateParams, Tweet) {
                    return Tweet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tweet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tweet-detail.edit', {
            parent: 'tweet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet/tweet-dialog.html',
                    controller: 'TweetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tweet', function(Tweet) {
                            return Tweet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tweet.new', {
            parent: 'tweet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet/tweet-dialog.html',
                    controller: 'TweetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tweetText: null,
                                createdAt: null,
                                lang: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tweet', null, { reload: 'tweet' });
                }, function() {
                    $state.go('tweet');
                });
            }]
        })
        .state('tweet.edit', {
            parent: 'tweet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet/tweet-dialog.html',
                    controller: 'TweetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tweet', function(Tweet) {
                            return Tweet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tweet', null, { reload: 'tweet' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tweet.delete', {
            parent: 'tweet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet/tweet-delete-dialog.html',
                    controller: 'TweetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tweet', function(Tweet) {
                            return Tweet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tweet', null, { reload: 'tweet' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
