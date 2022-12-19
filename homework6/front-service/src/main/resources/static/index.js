(function () {
    angular
        .module('market', ['ngRoute', 'ngStorage', 'angularUtils.directives.dirPagination'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'store/store.html',
                controller: 'storeController'
            })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .when('/registration', {
                templateUrl: 'users/registration.html',
                controller: 'registrationController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    function run($rootScope, $http, $localStorage) {
        if (!$localStorage.guestCartId) {
            $http.get('http://localhost:5555/cart/api/v1/cart/new_uuid')
                .then(function successCallback(response) {
                    $localStorage.guestCartId = response.data.value;
                });
        }

        if ($localStorage.springWebUser) {
            try {
                let jwt = $localStorage.springWebUser.token;
                let payload = JSON.parse(atob(jwt.split('.')[1]));
                let currentTime = parseInt(new Date().getTime() / 1000);
                if (currentTime > payload.exp) {
                    console.log("Token is expired!!!");
                    delete $localStorage.springWebUser;
                    $http.defaults.headers.common.Authorization = '';
                }
            } catch (e) {
            }

            if ($localStorage.springWebUser) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.springWebUser.token;
                $rootScope.username = $localStorage.springWebUser.username;
            }
        }
    }
})();

angular.module('market').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {

    const contextPath = 'http://localhost:5555';

    $scope.user = {
        username: '',
        password: '',
    };

    $scope.tryToAuth = function () {
        $http.post(contextPath + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.springWebUser = {
                        username: $scope.user.username,
                        token: response.data.token,
                        roles: response.data.roles
                    };
                    $rootScope.username = $scope.user.username;
                    $scope.user.username = '';
                    $scope.user.password = '';

                    $http.get(contextPath + '/cart/api/v1/cart/' + $localStorage.guestCartId + '/merge')
                        .then(function (response) {
                        });

                    $location.path('/');
                }
            }, function errorCallback(response) {
                if (response.data) {
                    alert(response.data.error);
                }
            });
    }

    $scope.tryToLogout = function () {
        $scope.clearUser();
        $rootScope.username = null;
        $scope.user.username = '';
        $scope.user.password = '';
        $location.path('/');
    }

    $scope.clearUser = function () {
        delete $localStorage.springWebUser;
        $http.defaults.headers.common.Authorization = '';
    }

    $rootScope.isUserLoggedIn = function () {
        return !!$localStorage.springWebUser;
    }

    $rootScope.hasUserTheManagerRole = function () {
        return $localStorage.springWebUser ? $localStorage.springWebUser.roles.includes('ROLE_MANAGER') : false;
    }
});
