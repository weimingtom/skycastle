echo "#### Building executable jar"

mvn clean
mvn assembly:assembly

echo "#### Moving and renaming produced jar"
cp target/skycastle-executable.jar skycastle.jar

echo "#### Building done.  Assuming there were no errors, you can run the skycastle client with ./skycastle.sh"

