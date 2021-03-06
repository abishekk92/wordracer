/* Dynamic Game Board */

var gposa, gposb, gworda, gwordb;
var gcurmove = -1;
var gboarda, gboardb;

function gInit() {
  if (!document.getElementById) return;
  if (gposa) {
    gboarda = new Array();
    for (var i = 0; i < 49; i++) {
      gboarda[i] = document.getElementById("gba" + i);
      if (!gboarda[i]) return;
    }
  }
  if (gposb) {
    gboardb = new Array();
    for (var i = 0; i < 49; i++) {
      gboardb[i] = document.getElementById("gbb" + i);
      if (!gboardb[i]) return;
    }
  }
  if (gposa) gcurmove = gposa.length;
  if (gposb && gposb.length > gcurmove) gcurmove = gposb.length;
}

function gShowWord(board, dir, pos, len) {
  if (dir == "H") {
    board[pos].className = board[pos].className + "r";
    pos++;
    while (len-- > 2) {
      board[pos].className = board[pos].className + "lr";
      pos++;
    }
    board[pos].className = board[pos].className + "l";
  } else {
    board[pos].className = "b" + board[pos].className;
    pos += 7;
    while (len-- > 2) {
      board[pos].className = "tb" + board[pos].className;
      pos += 7;
    }
    board[pos].className = "t" + board[pos].className;
  }
}

function gHideWord(board, dir, pos, len) {
  if (dir == "H") {
    while (len-- > 0) {
      board[pos].className = (board[pos].className.replace("l","")).replace("r","");
      pos++;
    }
  } else {
    while (len-- > 0) {
      board[pos].className = (board[pos].className.replace("t","")).replace("b","");
      pos += 7;
    }
  }
}

function gDoMove(board, pos, word) {
  if (pos == null) return;
  board[pos].className = "";
  if (word)
    for (var i = 0; i < word.length; i++) {
      if (word[i]) {
        var a = word[i].split(" ");
        if (a[0] == "+")
          gShowWord(board, a[1], parseInt(a[2]), a[3].length);
        else
          gHideWord(board, a[1], parseInt(a[2]), a[3].length);
      }
    }
}

function gUndoMove(board, pos, word) {
  if (pos == null) return;
  if (word)
    for (var i = word.length-1; i >= 0; i--) {
      if (word[i]) {
        var a = word[i].split(" ");
        if (a[0] == "+")
          gHideWord(board, a[1], parseInt(a[2]), a[3].length);
        else
          gShowWord(board, a[1], parseInt(a[2]), a[3].length);
      }
    }
  board[pos].className = "empty";
}

function gSetMove(m) {
  if (gcurmove < 0) return;
  while (gcurmove < m) {
    if (gposa)
      gDoMove(gboarda, gposa[gcurmove]);
    if (gposb)
      gDoMove(gboardb, gposb[gcurmove]);
    gcurmove++;
  }
  while (gcurmove > m) {
    gcurmove--;
    if (gposa)
      gUndoMove(gboarda, gposa[gcurmove]);
    if (gposb)
      gUndoMove(gboardb, gposb[gcurmove]);
  }
}
