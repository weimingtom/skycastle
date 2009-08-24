#! /bin/sh

GAMEPATH=.
DARKSTAR_PATH=sgs

echo "Starting $GAMENAME"

java -jar $DARKSTAR_PATH/bin/sgs-boot.jar $GAMEPATH/conf/SkycastleServer.boot &
