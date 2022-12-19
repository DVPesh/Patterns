angular.module('market').controller('ordersController', function ($scope, $http) {

    const contextPath = 'http://localhost:5555';

    $scope.loadOrders = function () {
        $http.get(contextPath + '/core/api/v1/orders')
            .then(function (response) {
                $scope.myOrders = response.data;
            });
    }

    $scope.loadOrders();
});
