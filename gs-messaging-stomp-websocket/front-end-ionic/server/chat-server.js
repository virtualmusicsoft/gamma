var io = require('socket.io')(3000);
var stomp_connected = false

console.log('init.')

var Stomp = require('stompjs');
var destination = '/topic/noteon';
var stompClient = Stomp.overWS('ws://localhost:8080/note');

stomp_noteon_callback = function(payload) {
	console.log('Recebido via STOMP: ' + payload.body);
	// sending to all clients, include sender
    io.emit('nota', payload.body);
	//olhar em: http://stackoverflow.com/questions/10058226/send-response-to-all-clients-except-sender-socket-io
};

connect_callback = function() {
	console.log('Connected STOMP.');
	stompClient.subscribe('/topic/noteon', stomp_noteon_callback);
};

error_callback = function(error) {
	// display the error's message header:
	//alert(error.headers.message);
	console.log('Erro no STOMP.');
};

stompClient.connect({}, connect_callback, error_callback);

this.socketio_callback = function(socket){
	
	socket.on('join:room', function(data){
		var room_name = data.room_name;
		socket.join(room_name);
	});


	socket.on('leave:room', function(msg){
		msg.text = msg.user + ' has left the room';
		socket.leave(msg.room);
		socket.in(msg.room).emit('message', msg);
	});


	socket.on('send:message', function(msg){
		socket.in(msg.room).emit('message', msg);
	});
	
	console.log('Socket IO.');
	
};

io.on('connection', this.socketio_callback)