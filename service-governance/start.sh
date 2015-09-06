application_launcher='com.baidu.ub.msoa.governance.ServiceGovernanceLauncher'
application_name="service-governance"

server_root=`pwd`
classpath="$server_root/conf:$server_root/bundles/*:$server_root/lib/*"

java_opts="-Dcom.sun.management.jmxremote.port=3333 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
java_opts="$java_opts -Dcom.sun.management.snmp.interface=0.0.0.0 -Dcom.sun.management.snmp.port=1161 -Dcom.sun.management.snmp.acl=false"
java_opts="$java_opts -server -Xms256m -Xmx256m -Xmn128m -Xss256K -XX:MaxPermSize=64m -XX:ReservedCodeCacheSize=32m"
java_opts="$java_opts -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:ParallelGCThreads=2"
java_opts="$java_opts -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"
log_path="/tmp/service-governance.log"

PID=`ps -ef | grep $application_launcher | grep -v ' grep' | awk '{print $2}'`
if [ ! -e $PID ]
then
        echo "kill $application_name PID is $PID"
        kill $PID
else
        echo "$application_name not run"
fi

echo "java $java_opts -cp $classpath $application_launcher >> $log_path 2>&1 &"
java $java_opts -cp $classpath $application_launcher >> $log_path 2>&1 &