window.onload = function() {
    loadMap();
}

var url = "http://localhost:3000/";

function loadMap() {
   $.post( url, "{ :type :svg }", function (fromServer, status){
        $("#mapdiv").html( fromServer );
   });
}
