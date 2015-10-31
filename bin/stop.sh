#!/bin/bash

##################################################################################
#                                                                                #
#       Descrition:     Stop TcpServer                                           #
#       Author:         Ares.yi                                                  #
#       Createtime:     2013-07-29                                               #
#                                                                                #
##################################################################################

pushServerPID=`ps -ef | awk '/[c]om.ydj.pushserver.startup.Bootstrap/{print $2}'`

if [ "$pushServerPID" = "" ]; then
        echo "PushServer does not exist. Is PushServer running? Stop aborted."
        exit 1
fi

kill $pushServerPID

sleep 3

kill -9 $pushServerPID >/dev/null 2>&1