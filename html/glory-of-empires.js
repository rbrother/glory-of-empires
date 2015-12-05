window.onload = function() {
  RefreshView();
}

var url = "http://localhost:3000/";

function ExecuteCommand() {
  var command = "(command/" + $("#command").val() + ")";
  $("#currentCommand").html(command + " ➔ ");
  $.post( url, command, function (fromServer, status){
      $("#commandResult").html( fromServer );
      RefreshView();
  });
  $("#command").val("");
}

// Make view refresh periodically (only if game-state changed
function RefreshView() {
  var viewDef = "(view/" + $("#viewDefinition").val() + ")"; 
  $.post( url, viewDef, function (fromServer, status){
    $("#view").html( fromServer );
  });

}


// ---------------------------------------------------

// window.onload = function() { setInterval(animate, 40); }

// We can even make animations of SVG-elements
function animate() {
  laatta1 = $("#laatta1");
  laatta1.setAttribute("x",x);
  x = x + 1;
}

