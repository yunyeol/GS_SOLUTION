#!/bin/sh

APP_HOME=/app/source/engine
JAVA_HOME=/app/jdk1.8.0_191/bin
SERVER_NAME=ENGINE

HIP_MEMORY=512m
APP_PID=$APP_HOME/$SERVER_NAME.pid

case "$1" in
	start)
		if test -s "$APP_PID"
		then
			echo "Already $SERVER_NAME Running !" 
		else
			echo -n "Starting $SERVER_NAME Agent :"
			echo
			$JAVA_HOME/java -jar -D$SERVER_NAME -Xmx$HIP_MEMORY -Xms$HIP_MEMORY -XX:+UseParallelGC -XX:+UseCompressedOops -XX:+PrintFlagsFinal engine-0.0.1-SNAPSHOT.jar 	 > /dev/null 2>&1 &
			echo $! > $APP_PID
			echo
		fi
		;;

	run)
		echo -n "Start $SERVER_NAME Foreground"
		echo
		$JAVA_HOME/java -jar -D$SERVER_NAME -Xmx$HIP_MEMORY -Xms$HIP_MEMORY -XX:+UseParallelGC -XX:+UseCompressedOops -XX:+PrintFlagsFinal engine-0.0.1-SNAPSHOT.jar	
	;;

	stop)
		if test -s "$APP_PID"
		then
			SERVER_PID=`cat $APP_PID`
			echo "Killing $SERVER_NAME Agent : "
			echo
			kill $SERVER_PID
			rm -f $APP_PID
		else
			echo "No pid file found !" 
		fi
		echo
	;;
	restart)
		$0 stop
		$0 start
	;;
	*)
		echo "Usage: $0 {start|stop|restart}"
		exit 1
esac

exit 0
