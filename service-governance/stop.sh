application_launcher='com.baidu.ub.msoa.governance.ServiceGovernanceLauncher'
application_name="service-governance"

PID=`ps -ef | grep $application_launcher | grep -v ' grep' | awk '{print $2}'`
if [ ! -e $PID ]
then
        echo "kill $application_name PID is $PID"
        kill $PID
else
        echo "$application_name not run"
fi