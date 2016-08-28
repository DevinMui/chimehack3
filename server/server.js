var express = require("express")
var bodyParser = require("body-parser")
var app = express()
var ejs = require('ejs')
var FB = require('fb')

app.set('view engine', 'ejs');
app.use(bodyParser.json())
app.use(express.static('public'))

FB.setAccessToken('EAACEdEose0cBAHdBbp5ZCSzejc1s5jZBpwbi2vZACNec2awO9NpZAovxM89k5FBZBqNExD6x25SbKm5Y0V9KgyQATrmwZCLBLTHZCYpZAszuRJcu1ZCQMnMo2VEZBlcfE7CBDMHHQSgajylv0ySKJhhEP3OenIZCJT5Bs7JvXpXv1NiCQZDZD');

var lat, long;
var panic = false;

app.get('/', function(req, res){
    res.render("index", {lat: lat, long: long, panic: panic})    
})

app.post('/gps', function(req, res){
	lat = req.body.lat
	long = req.body.long
	panic = true
 
	var body = 'Help I might be getting raped! (this is clearly a test post for a hackathon please do not treat this as real) \n Situation: http://dhsrobotics.com:3000/';
	FB.api('me/feed', 'post', { message: body }, function (res) {
	  if(!res || res.error) {
	    console.log(!res ? 'error occurred' : res.error);
	    return;
	  }
	  console.log('Post Id: ' + res.id);
	});
	res.send({lat: lat, long: long})
})

app.get('/gps', function(req, res){
	if(panic)
		res.send({ lat: lat, long: long })
	else
		res.send({})
})

app.get('/nopanic', function(req, res){
	panic = false
	lat = undefined
	long = undefined
	res.send({})
})

app.listen(3000, function(){
    console.log("lmao")
})
