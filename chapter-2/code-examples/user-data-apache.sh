# Here is an example of installing an Apache web server on an Amazon Linux 2 instance
# with a shell script that is provided as the user data:
#!/bin/bash
yum update -y
yum install httpd -y
systemctl start httpd
systemctl enable httpd
