#!/bin/sh
docker network create \
	  --driver=bridge \
	  --subnet=172.1.0.0/16 \
	  --ip-range=172.1.1.0/24 \
	  --gateway=172.1.1.254 \
	  network-1
