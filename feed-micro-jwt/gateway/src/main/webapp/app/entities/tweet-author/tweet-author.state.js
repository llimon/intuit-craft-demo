(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tweet-author', {
            parent: 'entity',
            url: '/tweet-author',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TweetAuthors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tweet-author/tweet-authors.html',
                    controller: 'TweetAuthorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('tweet-author-detail', {
            parent: 'tweet-author',
            url: '/tweet-author/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TweetAuthor'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tweet-author/tweet-author-detail.html',
                    controller: 'TweetAuthorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TweetAuthor', function($stateParams, TweetAuthor) {
                    return TweetAuthor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tweet-author',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tweet-author-detail.edit', {
            parent: 'tweet-author-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet-author/tweet-author-dialog.html',
                    controller: 'TweetAuthorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TweetAuthor', function(TweetAuthor) {
                            return TweetAuthor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tweet-author.new', {
            parent: 'tweet-author',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet-author/tweet-author-dialog.html',
                    controller: 'TweetAuthorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                screenname: null,
                                name: null,
                                createdAt: null,
                                profileImageUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tweet-author', null, { reload: 'tweet-author' });
                }, function() {
                    $state.go('tweet-author');
                });
            }]
        })
        .state('tweet-author.edit', {
            parent: 'tweet-author',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet-author/tweet-author-dialog.html',
                    controller: 'TweetAuthorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TweetAuthor', function(TweetAuthor) {
                            return TweetAuthor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tweet-author', null, { reload: 'tweet-author' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tweet-author.delete', {
            parent: 'tweet-author',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet-author/tweet-author-delete-dialog.html',
                    controller: 'TweetAuthorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TweetAuthor', function(TweetAuthor) {
                            return TweetAuthor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tweet-author', null, { reload: 'tweet-author' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
