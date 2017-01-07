var viewCounter = -1; // used for detecting if game state has changed
var currentView = "board";
var refreshPeriod = 2000; // ms
var viewRefreshCount = 0;

window.onload = function() {
  LoadViewIfChanged();
}

function GameName() { return GetURLParameter("gameName"); }
function Role() { return GetURLParameter("role"); }
function Password() { return GetURLParameter("password"); }

function clicked(id) {
    console.log('-- Clicked on: ' + id);
    var name = id.substring( id.lastIndexOf("-") + 1 );
    $("#command").val( $("#command").val() + " " + name );
}

function ExecuteCommand() {
  var command = "(command/" + $("#command").val() + ")";
  $("#currentCommand").html(command);
  $("#commandResult").html( "" )
  ExecuteCommandInner( GameName(), Role(), quoted(Password()), command, function(fromServer) {
    $("#commandResult").html( fromServer );
    ReloadViewNow();
  } )
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
    window.open( "view" + window.location.search + "&view=" + $("#viewDefinition").val() );
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
    var message = BuildMessage(GameName(), Role(), Password(), "info", "(game-state/game-counter)");
    PostMessage( message, ViewCounterReceived );
}

function ViewCounterReceived(serverResponse) {
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
    LoadViewInner(GameName(), Role(), Password(), currentView, "view", true);
}
