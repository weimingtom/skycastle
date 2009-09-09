#! /bin/sh

GAMENAME=skycastle
SCRIPTPATH=$(readlink -f $( dirname $0 ) ) 
GAMEPATH=$SCRIPTPATH
DARKSTAR_PATH=$SCRIPTPATH/sgs

echo "Stopping Skycastle Server for game: $GAMENAME"

java -jar $DARKSTAR_PATH/bin/sgs-stop.jar $GAMEPATH/conf/SkycastleServer.boot
