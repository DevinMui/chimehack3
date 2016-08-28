var express = require("express")
var bodyParser = require("body-parser")
var app = express()

var lat, long;

app.get('/', function(req, res){
    res.send("ayy lmao")    
})

app.post('/gps', function(req, res){
	lat = req.body.lat
	long = req.body.long
	res.send({lat: lat, long: long})
})

app.listen(3000, function(){
    console.log("lmao")
})
