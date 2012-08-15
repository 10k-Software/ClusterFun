#!/bin/bash

java -Xms64m -Xmx612m -Djava.library.path=./lib/native/macosx/ -Dorg.lwjgl.opengl.Display.allowSoftwareOpenGL=true -jar ClusterFun.jar