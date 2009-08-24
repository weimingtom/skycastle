#! /bin/sh

echo "Creating Keystore"

keytool -genkey -dname "cn=N/A, ou=N/A, o=N/A, c=N/A" -keystore "skycastle-keystore" -alias skycastle -validity 9001


