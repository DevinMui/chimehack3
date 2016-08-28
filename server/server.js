var express = require("express")
var bodyParser = require("body-parser")
var app = express()
var ejs = require('ejs')

app.set('view engine', 'ejs');
app.use(bodyParser.json())
app.use(express.static('public'))

var lat, long;
lat = 37.4530
long = -122.1817
var panic = false;

app.get('/', function(req, res){
    res.render("index", {lat: lat, long: long, panic: panic})    
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
