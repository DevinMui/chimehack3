(function ($) {
    "use strict";

    var data = [];

    // Outputs some logs about jwplayer
    function print(t,obj) {
        for (var a in obj) {
            if (typeof obj[a] === "object") print(t+'.'+a,obj[a]);
            else data[t+'.'+a] = obj[a];
        }
    }

    $(document).ready(function () {

        var streamUri = "rtmp://192.168.1.40:1935/foobar/test.stream";

	     startPlayer(streamUri);

        $('#start').click(function () {
	         startPlayer(streamUri);         
        });
        $('#stop').click(function () {
            jwplayer('player').stop();            
        });
    });

    // Starts the flash player
    function startPlayer(stream) {

        jwplayer('player').setup({
            height: 480,
            width: 640,
   	      sources: [{
                file: stream
            }],
            rtmp: {
                bufferlength: 3
            }
        });

	     jwplayer("player").onMeta( function(event){
            var info = "";
            for (var key in data) {
                info += key + " = " + data[key] + "<BR>";
            }
            document.getElementById("status").innerHTML = info;

            print("event",event);

        });

        jwplayer('player').play();

    }

}(jQuery));
