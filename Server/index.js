var find = require('lodash');
var express = require('express');
var app = express();
var http = require('http').createServer(app).listen(3000, function () {
	console.log('server running port 3000');
});
var io = require('socket.io')(http);
var emailCheck = require('email-check');
var fs = require('fs');

/*
format profileAccount
{
	account: name,
	socket:socket

}
*/
/*
	code error
	100 success
	101 already
	102 email error
	103 invaild password or account not exist
*/
/*
	userOnlien  {
		username
		socket
		account
	}
*/
//define code error
const ERROR_SUCCESS = 100;
const ERROR_ALREADY = 101;
const ERROR_MAIL = 102;
const ERROR_INVAILD = 103;

var userOnline = [];

// when there is person connect
io.on('connection', function (socket) {

	// when there is person sign up
	socket.on('signUp', function (data) {
		console.log(data);

		// check exitst mail
		emailCheck(data.account)
		.then(function (res) {
			console.log(res);
			if(res){
				// when email invaild
				console.log(res);
				fs.readFile(__dirname +'/data/users.json','utf8', function(err,users) {
					var objectUsers = JSON.parse(users);
					if(find.findIndex(objectUsers.users, {account:data.account}) ===-1){
						// when account not exists
						socket.emit('resultSignUp', {
							status: ERROR_SUCCESS
						});
						objectUsers.list.push({account:data.account, password:data.password});
						fs.writeFile(__dirname+'/data/users.json', JSON.stringify(objectUsers, null, 2),'utf8', function (err) {
						console.log(err);
						});
					}
					else {
						// when account already exists
						socket.emit('resultSignUp',{
							status: ERROR_ALREADY
						});
					}

				});
			}
			else{
				console.log("thua");
				// when email not invaild
				socket.emit('resultSignUp', {
					status: ERROR_MAIL
				});

			}
		})
		.catch(function (err) {
			console.log(err);
			socket.emit('resultSignUp', {
				status: ERROR_MAIL
			})
		});
	});

	// when there is login
	socket.on('login', function (data) {
		console.log(data);
		fs.readFile(__dirname +'/data/users.json','utf8', function(err,users) {
			console.log(users);
			var objectUsers = JSON.parse(users);
			if(find.findIndex(objectUsers.list, {account:data.account, password:data.password})!==-1){
				console.log('Thanh cong');
				socket.emit('resultLogin', {
					status:ERROR_SUCCESS
				});
			}
			else{
				console.log('that bai');
				socket.emit('resultLogin', {
					status:ERROR_INVAILD
				});
			}
			});
	});




	socket.on('sendMessage', function (data) {
			// data = {username : name , data : dat}
			var indexReceiver = find.findIndex(userName, {username:data.username});
			var usernameReceiver = userName[indexReceiver];

	});
	console.log('co nguoi connect');
});
