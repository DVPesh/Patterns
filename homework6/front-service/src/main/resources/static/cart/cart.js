angular.module('market').controller('cartController', function ($rootScope, $scope, $http, $localStorage) {

    const contextPath = 'http://localhost:5555';

    $scope.loadCart = function () {
        $http.get(contextPath + '/cart/api/v1/cart/' + $localStorage.guestCartId)
            .then(function (response) {
                $scope.cart = response.data;
            });
    }

    $scope.checkOut = function () {
        $http({
            url: contextPath + '/core/api/v1/orders',
            method: 'POST',
            data: $scope.orderDetails
        }).then(function (response) {
            $scope.loadCart();
            $scope.orderDetails = null;
        });
    }

    $scope.disabledCheckOut = function () {
        alert("Для оформления заказа необходимо войти в учетную запись.");
    }

    $rootScope.addToCart = function (productId) {
        $http.get(contextPath + '/cart/api/v1/cart/' + $localStorage.guestCartId + '/add/' + productId)
            .then(function (response) {
                $scope.loadCart();
            });
    }

    $scope.subtractFromCart = function (productId) {
        $http.get(contextPath + '/cart/api/v1/cart/' + $localStorage.guestCartId + '/subtract/' + productId)
            .then(function (response) {
                $scope.loadCart();
            });
    }

    $scope.removeFromCart = function (productId) {
        $http.get(contextPath + '/cart/api/v1/cart/' + $localStorage.guestCartId + '/remove/' + productId)
            .then(function (response) {
                $scope.loadCart();
            });
    }

    $scope.clearCart = function () {
        $http.get(contextPath + '/cart/api/v1/cart/' + $localStorage.guestCartId + '/clear')
            .then(function (response) {
                $scope.loadCart();
            });
    }

    $scope.loadCart();
});
