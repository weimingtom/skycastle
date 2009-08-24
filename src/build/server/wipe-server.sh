#! /bin/sh

GAMEPATH=.

echo "### DELETING THE WHOLE DATABASE FOR THE SKYCASTLE SERVER AT $GAMEPATH!"
echo "## ABORT THE SCRIPT WITH Ctrl-C TO STOP, ENTER TO CONTINUE"
read affirmation

echo "## Removing database files"
rm -rv $GAMEPATH/data/SkycastleServer/dsdb/*

