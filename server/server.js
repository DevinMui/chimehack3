var express = require("express")
var bodyParser = require("body-parser")
var app = express()

app.use(bodyParser.json())
app.use(express.static('public'))

var lat, long;
var panic = false;

app.get('/', function(req, res){
    res.sendfile("index.html")    
})

app.post('/gps', function(req, res){
	lat = req.body.lat
	long = req.body.long
	panic = true
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
	res.send({})
})

app.listen(3000, function(){
    console.log("lmao")
})
