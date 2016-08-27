var express = require("express")
var bodyParser = require("body-parser")
var app = express()

app.get('/', function(req, res){
    res.send("ayy lmao")    
})

app.listen(3000, function(){
    console.log("lmao")    
})
