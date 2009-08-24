#! /bin/sh

GAMEPATH=.
DARKSTAR_PATH=sgs

echo "Stopping Skycastle Server"

java -jar $DARKSTAR_PATH/bin/sgs-stop.jar $GAMEPATH/conf/SkycastleServer.boot
