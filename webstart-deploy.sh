#! /bin/sh

###########################################################################

appname=skycastle
version=`cat version`
builddir=build
webstartdir=$builddir/skycastle-$version-webstart

echo "### Deploying webstart package of $webstartdir.  First we copy the webstart package to the correct SVN directory, after that you should still check in the changes yourself.  Assumes that you checked out flowpaint from the directory above trunk, so that the webstart directory is included."

echo "### Specify the directory to deploy the webstart into (unstable or stable).  WARNING: Deploying in stable will push the release to ALL USERS, use only for tested official releases."
echo ""
echo -n "### Enter 'unstable' or 'stable':  "
read deploydir
echo ""

deploypath=../webstart/$deploydir/

echo "### Copying contents of $webstartdir to $deploypath"
cp -v $webstartdir/* $deploypath

echo "### Setting webstart jnlp mime type to the correct value using svn properties"
svn propset svn:mime-type application/x-java-jnlp-file $deploypath/$appname.jnlp 

echo "### Done.  Remember to check in the files in $deploypath"

