var Stomp = require('stompjs');
var destination = '/topic/noteon';
var stompClient = Stomp.overWS('ws://localhost:8080/note');

var connect_callback = function() {
	console.log('Connected.');
	stompClient.subscribe(destination, function(payload){
		console.log('Recebido: ' + payload.body);
	});
};

var error_callback = function(error) {
	// display the error's message header:
	//alert(error.headers.message);
	console.log('Erro: ');
};

stompClient.connect({}, connect_callback, error_callback);