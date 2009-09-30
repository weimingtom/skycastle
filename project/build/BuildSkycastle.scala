
import sbt._


/**
 * SBT build script for Skycastle.
 * 
 * @author Hans Haggstrom
 */
class BuildSkycastle(info: ProjectInfo) extends DefaultProject(info)
{

  // Add some repositories we use
  val javaNetRepository = "java.net Maven2 Repository" at "http://download.java.net/maven/2/"

  // Dependencies
  val junitLib = "junit" % "junit" % "4.4"
  val Lib = "" % "" % ""
  val Lib = "" % "" % ""
  val Lib = "" % "" % ""
  val Lib = "" % "" % ""
  val Lib = "" % "" % ""





    <!-- Testing -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.4</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.specs</groupId>
      <artifactId>specs</artifactId>
      <version>1.2.5</version>
      <scope>test</scope>
    </dependency>

    <!-- Scala -->
    <dependency>
      <groupId>org.scala-lang</groupId>
      <artifactId>scala-library</artifactId>
      <version>${scala.version}</version>
    </dependency>

    <!-- Darkstar Middleware -->
    <dependency>
      <groupId>com.projectdarkstar.server</groupId>
      <artifactId>sgs-server-api</artifactId>
      <version>${darkstar.version}</version>
    </dependency>

    <!--
        <dependency>
          <groupId>com.projectdarkstar.server</groupId>
          <artifactId>sgs-server</artifactId>
          <version>${darkstar.version}</version>
        </dependency>

        <dependency>
          <groupId>com.projectdarkstar.server</groupId>
          <artifactId>sgs-server-internal-api</artifactId>
          <version>${darkstar.version}</version>
        </dependency>
    -->

    <dependency>
      <groupId>com.projectdarkstar.server</groupId>
      <artifactId>sgs-shared</artifactId>
      <version>1.8</version>
    </dependency>

    <dependency>
      <groupId>com.projectdarkstar.client</groupId>
      <artifactId>sgs-client-api</artifactId>
      <version>${darkstar.version}</version>
    </dependency>

    <dependency>
      <groupId>com.projectdarkstar.client</groupId>
      <artifactId>sgs-client</artifactId>
      <version>${darkstar.version}</version>
    </dependency>


    <!-- Darkstar client dependencies -->
    <dependency>
      <groupId>org.apache.mina</groupId>
      <artifactId>mina-core</artifactId>
      <version>1.1.7</version>
    </dependency>

    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
      <version>1.5.6</version>
    </dependency>

    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-jdk14</artifactId>
      <version>1.5.6</version>
    </dependency>

    <!-- Java Monkey Engine -->
    <!--
        <dependency>
            <groupId>jme</groupId>
            <artifactId>jme</artifactId>
            <version>2.0</version>
        </dependency>
    -->
    <dependency>
      <groupId>com.projectdarkstar.ext.com.jmonkeyengine</groupId>
      <artifactId>jme</artifactId>
      <version>2.0-S1</version>
    </dependency>
    <dependency>
      <groupId>com.projectdarkstar.ext.com.jmonkeyengine</groupId>
      <artifactId>jme-terrain</artifactId>
      <version>2.0-S1</version>
    </dependency>

    <!-- JME Dependencies -->
    <dependency>
      <groupId>com.projectdarkstar.ext.org.lwjgl</groupId>
      <artifactId>lwjgl</artifactId>
      <version>2.0rc2</version>
    </dependency>
    <dependency>
      <groupId>com.projectdarkstar.ext.org.lwjgl</groupId>
      <artifactId>lwjgl_util</artifactId>
      <version>2.0rc2</version>
    </dependency>
    <!--
    <dependency>
      <groupId>com.projectdarkstar.ext.org.lwjgl</groupId>
      <artifactId>lwjgl_util_applet</artifactId>
      <version>2.0rc2</version>
    </dependency>
-->
<!--
    <dependency>
      <groupId>org.lwjgl</groupId>
      <artifactId>lwjgl</artifactId>
      <version>2.0.1</version>
    </dependency>
    <dependency>
      <groupId>org.lwjgl</groupId>
      <artifactId>lwjgl_util_applet</artifactId>
      <version>2.0.1</version>
    </dependency>
    <dependency>
      <groupId>org.lwjgl</groupId>
      <artifactId>lwjgl_util</artifactId>
      <version>2.0.1</version>
    </dependency>
-->
    <!--
    <dependency>
      <groupId>net.java.dev.jogl</groupId>
      <artifactId>jogl</artifactId>
      <version>1.1.1</version>
    </dependency>
    -->

    <!--
    <dependency>
        <groupId>net.java.dev.gluegen</groupId>
        <artifactId>gluegen-rt</artifactId>
        <version>1.0b06</version>
    </dependency>
    -->
    <dependency>
      <groupId>net.java.dev.jinput</groupId>
      <artifactId>jinput</artifactId>
      <version>0</version>
    </dependency>

    <dependency>
      <groupId>com.projectdarkstar.ext.jorbis</groupId>
      <artifactId>jorbis</artifactId>
      <version>0.0.17</version>
    </dependency>
<!--
    <dependency>
      <groupId>jorbis</groupId>
      <artifactId>jorbis</artifactId>
      <version>0.0.17</version>
    </dependency>
-->
    <!-- Primitive collections -->
    <dependency>
      <groupId>trove</groupId>
      <artifactId>trove</artifactId>
      <version>2.0.4</version>
    </dependency>

    <!-- Geometry -->
    <dependency>
      <groupId>com.vividsolutions</groupId>
      <artifactId>jts</artifactId>
      <version>1.8</version>
    </dependency>

    <!-- Tablet support -->
    <dependency>
      <groupId>jpen</groupId>
      <artifactId>jpen</artifactId>
      <version>2-081201</version>
    </dependency>

    <!-- Noise functions and other fun stuff
    <dependency>
      <groupId>j3d-org</groupId>
      <artifactId>j3d-org-java3d-all</artifactId>
      <version>0.9</version>
    </dependency>
    -->

    <!-- Opening a webpage in a browser from Java -->
    <dependency>
      <groupId>browserlauncher2</groupId>
      <artifactId>browserlauncher2</artifactId>
      <version>1.3</version>
    </dependency>

    <!-- A custom layout manager -->
    <dependency>
      <groupId>com.miglayout</groupId>
      <artifactId>miglayout</artifactId>
      <version>3.6</version>
    </dependency>

    <!-- Runtime Java compiler -->
<!--
    <dependency>
      <groupId>janino</groupId>
      <artifactId>janino</artifactId>
      <version>2.5.15</version>
    </dependency>
-->
  </dependencies>

}

