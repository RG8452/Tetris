# Tetris
My junior year of high school I was bored and wanted to program a game and being the creative person I am I chose Tetris, which no one has ever done before. This is that game.

First I have to express that this was not created in Eclipse or IntelliJ, but instead in JCreator (hence the awful format). If you have to run it in one of those environments, check out the eclipse branch (which is a mostly functional adaptation).

The game begins by asking the user for a pixel size (32, 40, 48). This was because I didn't know how to account for screen resolution, so I ask the user for an arbitrary value that is used to scale all the things on the screen. The game uses .wav files sourced from youtube to render music (using com.sun libraries) and the starting menu also has a button that toggles the difficulty of the game. Easy mode is straight-forward Tetris, while hard mode removes all the color from each Tetromino while also removing the ghost piece and starting the game at a higher pace.

Simply described, the game uses KeyListeners and MouseListeners to handle user input. The board itself is a 22 row by 10 column array of Colors (representing Tetromino blocks) and whenever a full row has no black colors in it, then the row is cleared. Each row cleared gives a Line, which is counted and gives score proportional to Level. Enough Lines increase the level, which in turn increases the game's natural speed. The UI shows the next three pieces, the currently stored piece, the count of every piece placed so far, the lines cleared, the current level, the time passed, and the total score earned. The pieces fall automatically, and the player can speed them up, hard drop them, and move them with the arrow keys (down, up, left/right respectively). Q and E rotate the pieces counter-clockwise and clockwise, and W stores a piece. The game speeds up until the player cannot continue clearing away lines. Once a piece cannot be placed without overwriting a placed piece (i.e. the player has filled all the way to the 0th row), the player hits a Game Over and gets the option to restart.

Each individual grouping of four blocks is called a Tetromino. Each quarter of it is a Block object. Tetromino is abstracted (because all tetrominos move left, right down, and drop the same way) and then extended for each of the seven unique block types (Line (I), L, J, Z, S, T, and Square (O)) which all rotate differently. These in turn are passed to the board, allowing the user to play.

Again, hopefully you can piece through my Programming II code and deal with my JCreator default package to a usable extent. Ask away if you've got any questions.

-Ryan Gahagan
