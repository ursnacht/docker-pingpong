#!/bin/sh
docker network create \
	  --driver=bridge \
	  --subnet=172.2.0.0/16 \
	  --ip-range=172.2.2.0/24 \
	  --gateway=172.2.2.254 \
	  network-2
	  
	  