#!/bin/bash

##################################################################################
#                                                                                #
#       Descrition:     Start TcpServer                                          #
#       Author:         Ares.yi                                                  #
#       Createtime:     2013-07-29                                               #
#                                                                                #
##################################################################################

PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`

[ -z "$SERVER_HOME" ] && SERVER_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

logpath=$SERVER_HOME/log

tempClassPath=$CLASSPATH;

flist=$SERVER_HOME/lib/*.jar;

for i in  $flist;
do
mypath=$i;
CLASSPATH=$CLASSPATH:$mypath;
done

CLASSPATH=$CLASSPATH:$tempClassPath;

export CLASSPATH

cd $SERVER_HOME/bin

nohup java -server \
	-Xmx2048M \
	-Xms2048M \
	-Xmn500M \
	-XX:PermSize=256M \
	-XX:MaxPermSize=256M \
	-XX:SurvivorRatio=1 \
	-XX:+UseConcMarkSweepGC \
	-XX:MaxTenuringThreshold=10 \
	-XX:+CMSParallelRemarkEnabled \
	-XX:+UseCMSCompactAtFullCollection \
	-XX:CMSMaxAbortablePrecleanTime=500 \
	-XX:+CMSClassUnloadingEnabled \
	-XX:+PrintClassHistogram \
	-XX:+PrintGCDetails \
	-XX:+PrintGCTimeStamps \
	-XX:+PrintGCApplicationStoppedTime \
	-XX:+PrintHeapAtGC \
	-XX:+DisableExplicitGC \
	-Xloggc:$logpath/gc.log \
	-XX:+HeapDumpOnOutOfMemoryError \
	-XX:HeapDumpPath=$logpath  \
	-Djava.rmi.server.hostname=61.129.xx.xx \
        -Dcom.sun.management.jmxremote \
        -Dcom.sun.management.jmxremote.port=10278 \
        -Dcom.sun.management.jmxremote.ssl=false \
        -Dcom.sun.management.jmxremote.authenticate=false \
	com.ydj.pushserver.startup.Bootstrap >$logpath/out.log 2>&1 &