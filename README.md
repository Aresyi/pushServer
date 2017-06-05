# pushServer
PushServer is a simple,light push server that mainly using `Netty` implementation.
<br>
<br>
* Deployment structure:
  * bin(Class&shell)
    * com
    * startup.sh
    * shutdown.sh
  * conf(Configuration file)
    * p12file
    * xxxx.xml
    * sys.properties
    * log4j.properties
  * lib(Depends jar)
  * logs(Log output directory)
* Start and close the service：
  * start: /bin/startup.sh
  * shutdown: /bin/shutdown.sh
* Timing diagram:
![](https://raw.githubusercontent.com/Aresyi/pushServer/master/doc/use.gif)
