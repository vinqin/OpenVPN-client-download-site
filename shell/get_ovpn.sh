#! /usr/bin/expect

set clientname [lindex $argv 0]

spawn ssh root@10.39.27.199

expect "*password:*"
# below password is a sample.
send "password11\r"

expect "*#"
send "cd /root/openvpn-client-configs\r"

expect "*#"
send "./make_config.sh $clientname\r"

expect "*#"
send "scp files/${clientname}.ovpn duncan@10.39.27.125:/var/tomcat/ovpn-client-files\r"

expect "*password:*"
# below password is a sample.
send "password22\r"

expect "*#"
send "exit\r"
#interact
