#rm coverage.em
#rm coverage.ec

cd ant

ant compile-test

cd ..

java -cp lib/emma.jar:build/classes/ emma instr -ip build/classes/ -m overwrite -filter -*Test -filter -junit*

java -Xms64m -Xmx612m -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ clusterfun.app.CFApp -c
java -Xms64m -Xmx612m -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ clusterfun.app.CFApp -nd

java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.board.CardTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.board.DeskTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.board.GameBoardTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.logic.CFLogicTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.message.MessageManagerTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.player.HumanPlayerTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.state.CFStateTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.ui.image.CardImageTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.ui.CardEntityTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.state.ScoreTest
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ junit.textui.TestRunner clusterfun.state.HallOfFameTest

java -Xms64m -Xmx612m -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ clusterfun.app.CFAppDriver < test/data/oracles/CFApp.input.real
java -Xms64m -Xmx612m -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ clusterfun.ui.GameWindowDriver
java -Xms64m -Xmx612m -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ clusterfun.sounds.CFSoundsDriver
java -Xms64m -Xmx612m -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ clusterfun.ui.GraphicUIDriver
java -Djava.library.path=./lib/native/linux/ -cp lib/jinput.jar:lib/lwjgl.jar:lib/lwjgl_util.jar:lib/emma.jar:lib/junit-4.1.jar:build/classes/ clusterfun.ui.TextUIDriver < test/data/oracles/TextUI.input.fake

java -cp lib/costello.jar:/emma.jar:lib/:build/classes/ junit.extensions.abbot.ScriptTestSuite test/data/ThemeTestScript


java -cp lib/emma.jar emma report -r html -in coverage.em -in coverage.ec -sp src
