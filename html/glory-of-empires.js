var dataUrl = 'http://empires.brotherus.net/glory-of-empires/'

var url = "http://empires.brotherus.net/empires";

var viewCounter = -1; // used for detecting if game state has changed
var currentView = "board";
var refreshPeriod = 2000; // ms
var viewRefreshCount = 0;

$.ajaxSetup({
    type: 'POST',
    timeout: 5000,
    error: function(xhr) {
        console.log('Timeout from AJAX, trying again soon...');
        ScheduleViewRefresh( 5000 );
    }});

// *************** Functions serving the main glory-of-empires.html ***************

function ExecuteCommand() {
  var command = "(command/" + $("#command").val() + ")";
  $("#currentCommand").html(command);
  $("#commandResult").html( "" )
  var message = BuildMessage("command", command);
  $.post( url, message, function (fromServer, status){
      $("#commandResult").html( " ➔ " + fromServer );
      ReloadViewNow(); 
  });
}

function ExampleChanged() {
    $("#command").val( $("#examples option:selected").text() );
}

function ViewExampleChanged() {
    $("#viewDefinition").val( $("#viewExamples option:selected").text() );
    ViewDefinitionChanged();
}

function ViewDefinitionChanged() {
    currentView = $("#viewDefinition").val();
    viewCounter = -1; // Force reload on next refresh
}

function OpenView() {
    window.open( dataUrl + "view.html?view=" + $("#viewDefinition").val() );
}

// ***************** Shared functions for all windows *****************

function quoted(s) { return "\"" + s + "\""; }

function BuildMessage(messageType, func) {
    return "{ :game " + $("#game option:selected").text() + 
            " :role " + $("#role option:selected").text() + 
            " :password " + quoted( $("#password").val() ) + 
            " :message-type :" + messageType + 
            " :func " + func + " }";
}

function clicked(id) {
    console.log('-- Clicked on: ' + id);
}

function ScheduleViewRefresh( time ) {
    // Condition to prevent multiple refresh-loops being started if we
    // already have a view refresh pending
    if ( viewRefreshCount < 1 ) {
        window.setTimeout( LoadViewIfChanged, time );
        viewRefreshCount = viewRefreshCount + 1;
    }
}

function LoadViewIfChanged() {
    viewRefreshCount = viewRefreshCount - 1;    
    var message = BuildMessage("info", "(game-state/game-counter)");
    $.post( url, message, ViewCounterReceived );
}

function ViewCounterReceived(serverResponse, status) {
    var newCount = parseInt(serverResponse);    
    if (newCount == viewCounter) {
        console.log('View up-to-date ' + Date());
        ScheduleViewRefresh( refreshPeriod );
    } else {
        viewCounter = newCount;
        ReloadViewNow();
    }
}

function ReloadViewNow() {
    LoadViewInner(currentView, "view", true);
    if ($("#RoleSelector").length > 0) {
        LoadViewInner("role-selector", "RoleSelector", false);
    }
}

function LoadViewInner(view, targetWidgetId, scheduleNew) {
    var message = BuildMessage("view", "(view/" + view + ")");
    console.log('posting: ' + message);
    $.post( url, message, function (fromServer, status){
        console.log('received view from server');
        $("#" + targetWidgetId).html( fromServer );
        if (scheduleNew) {
            ScheduleViewRefresh( refreshPeriod );
        }
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
