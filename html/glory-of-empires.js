var url = decodeURIComponent(window.location.origin + window.location.pathname); // eg. "http://localhost:3000" + "/game"

$.ajaxSetup({
    type: 'POST',
    timeout: 5000,
    error: function(xhr) {
        console.log('Timeout from AJAX, trying again soon...');
        ScheduleViewRefresh( 5000 );
    }});

// ***************** Shared functions for all windows *****************

function PostMessage( message, afterFunc ) {
  console.log('posting: ' + message + " to " + url);
  $.post( url, message, function (fromServer, status) { afterFunc(fromServer); });
}

function quoted(s) { return "\"" + s + "\""; }

function BuildMessage(gameName, role, password, messageType, func) {
    var message = "{";
    if (gameName != "") message = message + " :game " + gameName;
    if (role != "") message = message + " :role " + role;
    if (password != "") message = message + " :password " + password;
    message = message +
      " :message-type :" + messageType +
      " :func " + func + " }";
    return message;
}

function clicked(id) {
    console.log('-- Clicked on: ' + id);
}

function ExecuteCommandInner(gameName, role, password, command, postFunction ) {
  var message = BuildMessage(gameName, role, password, "command", command);
  PostMessage( message, postFunction );
}

function LoadViewInner(gameName, role, password, view, targetWidgetId, scheduleNew) {
    var message = BuildMessage(gameName, role, password, "view", "(view/" + view + ")");
    PostMessage( message, function (fromServer) {
        console.log('received view from server');
        $("#" + targetWidgetId).html( fromServer );
        if (scheduleNew) {
            ScheduleViewRefresh( refreshPeriod );
        }
    });
}

function GetURLParameter( paramName ) {
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
