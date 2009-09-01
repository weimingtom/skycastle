#! /bin/sh

###########################################################################
# This is a shell script for generating a package for a release for a java project

# Application specific variables:
version=$(cat version)
revision=$(svnversion -n)
builddir=build
releaseDate=$(date +"%d %B %Y")
userReadableName=Skycastle
appName=skycastle
binDirName=$appName-$version-bin
serverDirName=$appName-$version-server-bin
srcDirName=$appName-$version-src
webstartDirName=$appName-$version-webstart
binaryPackageName=$binDirName.zip
serverPackageName=$serverDirName.zip
sourcePackageName=$srcDirName.zip
webstartPackageName=$webstartDirName.zip
googleCodeProjectId=skycastle
compiledJar=target/skycastle-executable.jar
jnlpFileLocation=src/build/webstart/skycastle.jnlp
issueReleasePrefix=Release-
keystoreFile=$appName-keystore
keystoreUser=$appName
appProperties=src/main/resources/META-INF/app.properties
homepage=www.skycastle.org
bugReportUrl=http://code.google.com/p/skycastle/issues/entry?template=Defect%20report%20from%20user
featureRequestUrl=http://code.google.com/p/skycastle/issues/entry?template=Feature%20request%20from%20user
oneLineDescription=$(cat short-description.txt)
credits="Programmed by Hans H&auml;ggstr&ouml;m ( zzorn @ iki.fi )"
license="GPL v2"
documentationUrl=http://skycastle.googlecode.com/svn/wiki/Documentation.wiki



echo "##### $userReadableName package creation script"
echo "##### Creating release packages for $userReadableName version $version, subversion revision $revision, release date $releaseDate"
echo "##### This script creates binary, source, and webstart packages for a release"
echo "##### It doesn't deploy them, but requires the passwords to the keystore used to sign the webstart files."

###########################################################################
# Start with asking the passwords, so that the process can run autonomously afterwards.
# The passwords are not passed as command line arguments, as that would leave them in the command history

echo -n "Keystore (and user $keystoreUser's) password for $keystoreFile: "
stty -echo
read storepass
stty echo
echo ""

# Assume same password for keystore user
keypass=$storepass
#echo -n "Keystore user password for user $keystoreUser: "
#stty -echo
#read keypass
#stty echo
#echo ""

###########################################################################
echo "#### Creating build directory '$builddir'"
mkdir $builddir

###########################################################################
echo "#### Updating application.properties"
scripts/set-property.py -q -f $appProperties -p "version" -v "$version"
scripts/set-property.py -q -f $appProperties -p "repositoryVersion" -v "$revision"
scripts/set-property.py -q -f $appProperties -p "releaseDate" -v "$releaseDate"
scripts/set-property.py -q -f $appProperties -p "homepage" -v "$homepage"
scripts/set-property.py -q -f $appProperties -p "bugReportUrl" -v "$bugReportUrl"
scripts/set-property.py -q -f $appProperties -p "featureRequestUrl" -v "$featureRequestUrl"
scripts/set-property.py -q -f $appProperties -p "applicationName" -v "$userReadableName"
scripts/set-property.py -q -f $appProperties -p "oneLineDescription" -v "$oneLineDescription"
scripts/set-property.py -q -f $appProperties -p "credits" -v "$credits"
scripts/set-property.py -q -f $appProperties -p "license" -v "$license"

###########################################################################
echo "#### Compiling"

mvn clean
mvn assembly:assembly

# TODO: Check exit status of compile - what's the idiom for that in shell script?

###########################################################################
echo "#### Generating changes.txt from completed issues"
scripts/change-lister.py --quiet --name $userReadableName --project $googleCodeProjectId --output $builddir/changes.txt --release $version --temp $builddir/temporary-issue-list.csv --prefix $issueReleasePrefix
rm $builddir/temporary-issue-list.csv

###########################################################################
echo "#### Downloading and generating readme.txt"
echo "### Generate title, version and release date information for readme.txt"
echo "= $userReadableName =" > $builddir/readme.txt
echo "" >> $builddir/readme.txt
echo "Documentation for $userReadableName version $version, released $releaseDate." >> $builddir/readme.txt
echo "### Downloading bulk of documentation for readme.txt from $documentationUrl"
# Append everything from the third line on (remove labels and original title)
wget -q $documentationUrl -O $builddir/temp_readme_file.txt
tail $builddir/temp_readme_file.txt -n +3 >> $builddir/readme.txt
rm $builddir/temp_readme_file.txt


###########################################################################
echo "#### Building source package"
echo "### Removing old package if found"
rm $builddir/$sourcePackageName

echo "### Removing old source package build directory"
rm -r $builddir/$srcDirName

echo "### Exporting sources"
svn export . $builddir/$srcDirName

echo "### Adding build files to sources"
mkdir $builddir/$srcDirName/scripts
cp scripts/* $builddir/$srcDirName/scripts/
cp license.txt $builddir/$srcDirName/
cp third-party-licenses $builddir/$srcDirName/
cp pom.xml $builddir/$srcDirName/
cp version $builddir/$srcDirName/

echo "### Adding generated files to sources"
cp $builddir/readme.txt $builddir/$srcDirName/
cp $builddir/changes.txt $builddir/$srcDirName/

echo "### Zipping source package up"
zip -r $builddir/$sourcePackageName $builddir/$srcDirName


###########################################################################
echo "#### Building binary package"
echo "### Removing old package if found"
rm $builddir/$binaryPackageName

echo "### Removing old binary package build directory"
rm -r $builddir/$binDirName

echo "### Making directory to collect the release contents to"
mkdir $builddir/$binDirName
mkdir $builddir/$binDirName/nativelibs

echo "### Collecting release contents"
cp $compiledJar $builddir/$binDirName/$appName.jar
cp src/build/client/* $builddir/$binDirName/
cp $builddir/readme.txt $builddir/$binDirName/
cp $builddir/changes.txt $builddir/$binDirName/
cp license.txt $builddir/$binDirName/
cp third-party-licenses $builddir/$binDirName/
cp nativelibs/*.dll $builddir/$binDirName/nativelibs/
cp nativelibs/*.so $builddir/$binDirName/nativelibs/
cp nativelibs/*.jnilib $builddir/$binDirName/nativelibs/

echo "### Setting shell script permissions in package"
chmod u+x $builddir/$binDirName/$appName.sh

echo "### Zipping binary package up"
zip -r $builddir/$binaryPackageName $builddir/$binDirName


###########################################################################
echo "#### Building server package"
echo "### Removing old package if found"
rm $builddir/$serverPackageName

echo "### Removing old server package build directory"
rm -r $builddir/$serverDirName

echo "### Making directory to collect the release contents to"
mkdir $builddir/$serverDirName
mkdir $builddir/$serverDirName/conf
mkdir $builddir/$serverDirName/deploy

echo "### Collecting release contents"
cp $compiledJar $builddir/$serverDirName/deploy/$appName.jar
cp src/build/server/* $builddir/$serverDirName/
cp src/build/serverconf/* $builddir/$serverDirName/conf/

cp $builddir/readme.txt $builddir/$serverDirName/
cp $builddir/changes.txt $builddir/$serverDirName/
cp license.txt $builddir/$serverDirName/
cp third-party-licenses $builddir/$serverDirName/

echo "### Setting shell script permissions in package"
chmod u+x $builddir/$serverDirName/*.sh

echo "### Zipping server package up"
zip -r $builddir/$serverPackageName $builddir/$serverDirName


###########################################################################
echo "#### Building webstart package"
echo "### Removing old package if found"
rm $builddir/$webstartPackageName

echo "### Removing old webstart package build directory"
rm -r $builddir/$webstartDirName

echo "### Making directory to collect the webstart contents to"
mkdir $builddir/$webstartDirName

echo "#### Copying files to webstart dir"
cp $jnlpFileLocation $builddir/$webstartDirName/$appName.jnlp
cp $compiledJar $builddir/$webstartDirName/$appName.jar

echo "#### Packaging native libraries"
cd nativelibs
jar cf ../$builddir/$webstartDirName/native-linux.jar *.so
jar cf ../$builddir/$webstartDirName/native-osx.jar *.jnilib
jar cf ../$builddir/$webstartDirName/native-win32.jar *.dll
cd ..

echo "#### Signing Jar:s"
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $builddir/$webstartDirName/$appName.jar $keystoreUser
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $builddir/$webstartDirName/native-linux.jar $keystoreUser
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $builddir/$webstartDirName/native-osx.jar $keystoreUser
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $builddir/$webstartDirName/native-win32.jar $keystoreUser

echo "### Zipping webstart package up (for easier transport to the target server)"
# Don't include the directory in the webstart package, as webstart is deployed by replacing the current content
cd $builddir/$webstartDirName
zip -r ../$webstartPackageName ./*
cd ../..

###########################################################################

echo "#### Cleaning up generated files"

rm $builddir/readme.txt 
rm $builddir/changes.txt


###########################################################################
echo "##### Done"

