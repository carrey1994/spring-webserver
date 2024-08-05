The docker-ci-tester.sh is for test on CI instead of local.
The .local-env file is encrypted and works with local-starter.sh to setup env variable for connecting cloud service, hence they are not public.
I uses supabase, redis cloud, and cloud amqp when developing on my local. 

There are ways to launch the server on local.
1. If Docker installed,
   Run "./docker-starter.sh -w".
   (The -w flag will pull image from dockerhub for backend server, otherwise script will launch the services except webserver.)
2. If Docker and K3d installed,
   Run "./k3d-starter.sh".
   (If Istio install, the flag '-i' is enable to launch with Istio.)
3. If Docker and Terraform installed,
   Run "./terraform-starter.sh".
![architecture.png](architecture.png)
