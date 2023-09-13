## devassoc-vpc : 	vpc-040983f27e724119c *10.0.0.0/23*
* public-subnet: subnet-0546461d1d04a0690 *10.0.0.0/28*
* private-subnet: subnet-0eee2c1abc29984d7 *10.0.0.64/26*

## webserver-no3 : i-00ebb69cb10158073
* Public IPv4 address : 18.143.135.96

## private instance :
* private IPv4 address : 10.0.0.118

## how to create a nat instance 
1. create an instance with AMI for NAT (amzn-nat-vpc-* in Community AI Section)
2. set up security group for NAT instance that allow SSH and HTTP/ HTTPS
3. stop check source / destination networking in NAT instance
4. update the route table of private subnet that 0.0.0.0/0 is the eni of NAT instance