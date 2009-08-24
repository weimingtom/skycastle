#! /bin/sh
###########################################################################

darkstar_version=0.9.10
darkstar_archive=sgs-server-dist-$darkstar_version
darkstar_url=http://www.projectdarkstar.com/distributions/$darkstar_version/sgs-server/$darkstar_archive.zip
darkstar_link=sgs

echo "### Downloading sun game server (project Darkstar) sever bundle version $darkstar_version from $darkstar_url"

wget $darkstar_url

echo "### Unzipping"

unzip $darkstar_archive.zip

echo "### Symlinking to $darkstar_link"

rm -f $darkstar_link
ln -s $darkstar_archive $darkstar_link

echo "### Removing downloaded archive"

rm $darkstar_archive.zip

echo "### Done"

