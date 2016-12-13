function GameSelected() {
  LoadViewInner($('#game option:selected').text(), '', '', 'role-selector', 'role-selector', false);
}

function CreateGame() {
  var gameName = ':' + $('#game').val();
  var gmPassword = quoted( $('#password').val() );
  var command = '(game-state/create-game ' + gameName + ' ' + gmPassword + ')';
  PostMessage( BuildMessage('','','', 'admin-command', command), function(fromServer) {
    if (fromServer == 'ok') {
      $('#result').html("Game created, proceeding to login page...");
      window.setTimeout( function() { window.location = '/login' }, 1500 );
    } else {
      $('#result').html(fromServer);
    }
  } );
}

