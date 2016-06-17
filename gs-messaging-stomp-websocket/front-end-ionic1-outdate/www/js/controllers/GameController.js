(function(){
  angular.module('starter')
    .controller('GameController', ['$scope', '$state', 'localStorageService', GameController]);

  function GameController($scope, $state, localStorageService){

    $scope.notas = ['init'];

    $scope.socket = {
      client: null,
      stomp: null
    };

    $scope.notify = function(message) {
      //TODO: fazer JSON.parse!
      $scope.notas.push(message.body);
      $scope.$apply();
    };

    $scope.reconnect = function() {
      setTimeout($scope.initSockets, 10000);
    };

    $scope.noteon = function(frame) {
      console.log('Connected: ' + frame);
      $scope.socket.stomp.subscribe("/topic/noteon", $scope.notify);
    }

    $scope.initSockets = function() {
      $scope.socket.client = new SockJS('http://localhost:8080/note');
      $scope.socket.stomp = Stomp.over($scope.socket.client);
      $scope.socket.stomp.connect({}, $scope.noteon);
      $scope.socket.client.onclose = $scope.reconnect;
    };

    $scope.initSockets();

  }

})();
