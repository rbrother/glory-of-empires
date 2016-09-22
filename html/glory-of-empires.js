var dataUrl = 'http://www.brotherus.net/glory-of-empires/'

var url = "http://empires.brotherus.net/empires";

function ExecuteCommand() {
  var command = "(command/" + $("#command").val() + ")";
  $("#currentCommand").html(command);
  $("#commandResult").html( "" )
  $.post( url, command, function (fromServer, status){
      $("#commandResult").html( " ➔ " + fromServer );
      RefreshView();
  });
}

function ExampleChanged() {
  $("#command").val( $("#examples option:selected").text() );
}

function ViewExampleChanged() {
  $("#viewDefinition").val( $("#viewExamples option:selected").text() );
  RefreshView();
}

function OpenView() {
  window.open( dataUrl + "view.html?view=" + $("#viewDefinition").val() );
}

function LoadView(viewDefinition) {
  var viewDef = "(view/" + viewDefinition + ")"; 
  console.log('posting: ' + viewDef);
  $.post( url, viewDef, function (fromServer, status){
	  console.log('received view from server');
      $("#view").html( fromServer );
  });
}

function GetURLParameter(paramName) {
	console.log('GetURLParameter: ' + paramName);
    var sPageURL = decodeURIComponent(window.location.search.substring(1));
    var sURLVariables = sPageURL.split('&');
	var result = '';
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == paramName) {
            return sParameterName[1];
        }
    }
}

// Make view refresh periodically (but update the view only if game-state changed)
function RefreshView() {
	LoadView( $("#viewDefinition").val() );
}

