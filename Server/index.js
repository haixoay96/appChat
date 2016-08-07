var find = require('lodash');
var express = require('express');
var app = express();
var http = require('http').createServer(app).listen(3000, function () {
	console.log('server running port 3000');
});
var io = require('socket.io')(http);
/*
format userName 
{
	username: name,
	socket:socket

}
*/

var userName = [];

io.on('connection', function (socket) {
	socket.on('login', function (data) {
		// data = {username:name}
		if(find.findIndex(userName, {username:data.username})!==-1){
			socket.emit('result', {status:false});
		}
		else{
			socket.emit('result', false);
		}

	});
	socket.on('sendMessage', function (data) {
			// data = {username : name , data : dat}
			var indexReceiver = find.findIndex(userName, {username:data.username});
			var usernameReceiver = userName[indexReceiver];
			usernameReceiver.socket.emit('receiveMessage',{username:});

	});
	console.log('co nguoi connect');
});
