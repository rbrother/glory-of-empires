window.onload = function() {
  RefreshView();
}

var url = "http://localhost:3000/";

function ExecuteCommand() {
  var command = $("#command").val();
  console.log("Executing: " + command);
  $("#currentCommand").html(command + " ➔ ");
  var wrappedCommand = "[:command " + command + "]";
  $.post( url, wrappedCommand, function (fromServer, status){
    $("#commandResult").val( fromServer );
  });
  $("#command").val("");
  RefreshView();
}

function RefreshView() {
  $.post( url, $("#viewDefinition").val(), function (fromServer, status){
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

