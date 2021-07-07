#! /bin/bash

javac -d build -Xlint:all groutboy/Coordinator.java
pushd build
jar cvfe ../Groutboy.jar groutboy.Coordinator *
popd
chmod +x Groutboy.jar
