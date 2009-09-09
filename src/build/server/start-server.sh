#! /bin/sh

GAMENAME=skycastle
SCRIPTPATH=$(readlink -f $( dirname $0 ) ) 
GAMEPATH=$SCRIPTPATH
DARKSTAR_PATH=$SCRIPTPATH/sgs

echo "Starting Skycastle Server with game: $GAMENAME"

java -jar $DARKSTAR_PATH/bin/sgs-boot.jar $GAMEPATH/conf/SkycastleServer.boot
