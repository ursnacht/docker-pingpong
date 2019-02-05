# docker-basics

Projekt zum Tüfteln von Docker, Docker Compose, etc.

# Verwendung des default Netzwerks "bridge"

    sc10:
      network_mode: bridge
      networks:
        default:
          aliases: 
            - c10.ch
            
    networks:
      default:
        external:
          name: bridge

--> network-scoped alias is supported only for containers in **user defined** networks

## Fazit:
--> eigenes (explizites) Netzwerk verwenden, wenn man mit alias arbeiten möchte.

Man kann aber auch ganz einfach die Aliase weglassen und stattdessen den andern Container mit dem Containernamen aufrufen. 

# Explizites, externes Netzwerk

network.sh

    #!/bin/sh
    docker network create \
	  --driver=bridge \
	  --subnet=172.1.1.0/16 \
	  --ip-range=172.1.1.0/24 \
	  --gateway=172.1.1.254 \
	  pingpong-1
	  
docker-compose.yml

    sc10:
      networks:
        default:
          aliases: 
            - c10.ch

    networks:
      default:
        external:
          name: pingpong-1

* Gateway: 172.1.1.254 resp. 172.2.2.254
* c10: 172.1.1.0:8080 (8010), c11: 172.1.1.1:8080 (8011)
* c20: 172.2.2.0:8080 (8020), c21: 172.2.2.1:8080 (8021)

## Aufrufe im eigenen Netzwerk
1. c10: <http://localhost:8010/ping> --> localhost + gemappter Port
2. c10: <http://172.1.1.0:8080/ping> --> IP-Adresse + interner Port
3. c10: <http://172.1.1.254:8010/ping> --> Gateway + gemappter Port


1. c11: <http://localhost:8011/ping>
2. c11: <http://172.1.1.1:8080/ping>
3. c11: <http://172.1.1.254:8011/ping>


1. c20: <http://localhost:8020/ping>
2. c20: <http://172.2.2.0:8080/ping>
3. c20: <http://172.2.2.254:8020/ping>


1. c10 --> c11: <http://localhost:8010/ping/172.1.1.1%3A8080>
2. c10 --> c11: <http://172.1.1.0:8080/ping/172.1.1.1%3A8080>
2. c10 --> c11: <http://172.1.1.254:8010/ping/172.1.1.1%3A8080>

## Aufruf mit Containername: c11
1. c10 --> c11: <http://localhost:8010/ping/c11%3A8080>
2. c10 --> c11: <http://172.1.1.0:8080/ping/c11%3A8080>
3. c10 --> c11: <http://172.1.1.254:8010/ping/c11%3A8080>

## Aufruf mit Servicename: sc11
1. c10 --> c11: <http://localhost:8010/ping/sc11%3A8080>
2. c10 --> c11: <http://172.1.1.0:8080/ping/sc11%3A8080>
3. c10 --> c11: <http://172.1.1.254:8010/ping/sc11%3A8080>

## Aufruf mit Alias: c11.ch
1. c10 --> c11: <http://localhost:8010/ping/c11.ch%3A8080>
2. c10 --> c11: <http://172.1.1.0:8080/ping/c11.ch%3A8080>
3. c10 --> c11: <http://172.1.1.254:8010/ping/c11.ch%3A8080>

## Calls
1. c10 --> c11: <http://localhost:8010/call/http%3A%2F%2Fc11%3A8080%2Fping>
2. c10 --> c11 --> c10: <http://localhost:8010/call/http%3A%2F%2Fc11%3A8080%2Fping%2Fc10%3A8080>

## Zwischen den Netzwerken

funktioniert nur mit Gateway oder via Host

### mit Gateway
1. c20: <http://172.2.2.0:8080/ping>
2. c10 --> c20: <http://localhost:8010/ping/172.2.2.0%3A8080> (direkt --> funktioniert nicht)
3. c10 --> c20: <http://localhost:8010/ping/172.2.2.254%3A8020> (mit Gateway --> funktioniert)

### via Host
1. c10 --> c20: <http://localhost:8010/ping/172.17.0.1%3A8020> (via Host --> funktioniert)

# Netzwerke ohne explizite Gateway

network.sh

    #!/bin/sh
    docker network create \
	  --driver=bridge \
	  --subnet=172.1.1.0/16 \
	  --ip-range=172.1.1.0/24 \
	  pingpong-1

docker-compose.yml

    sc10:
      networks:
        default:
          aliases: 
            - c10.ch

    networks:
      default:
        external:
          name: pingpong-1

* --> implizier Gateway 172.1.1.0 resp. 172.2.2.0
* c10: 172.1.1.1:8080 (8010), c11: 172.1.1.2:8080 (8011)
* c20: 172.2.2.1:8080 (8020), c21: 172.2.2.2:8080 (8021)

## Aufrufe im eigenen Netzwerk
1. c10: <http://localhost:8010/ping> --> localhost + gemappter Port
2. c10: <http://172.1.1.1:8080/ping> --> IP-Adresse + interner Port
3. c10: <http://172.1.1.0:8010/ping> --> Gateway + gemappter Port
4. c10: <http://172.17.0.1:8010/ping> --> Host + gemappter Port


1. c11: <http://localhost:8011/ping>
2. c11: <http://172.1.1.2:8080/ping>
3. c11: <http://172.1.1.0:8011/ping>


1. c20: <http://localhost:8020/ping>
2. c20: <http://172.2.2.1:8080/ping>
3. c20: <http://172.2.2.0:8020/ping>


1. c10 --> c11: <http://localhost:8010/ping/172.1.1.2%3A8080>
2. c10 --> c11: <http://172.1.1.1:8080/ping/172.1.1.2%3A8080>
2. c10 --> c11: <http://172.1.1.0:8010/ping/172.1.1.2%3A8080>

## Aufruf mit Containername: c11
1. c10 --> c11: <http://localhost:8010/ping/c11%3A8080>
2. c10 --> c11: <http://172.1.1.1:8080/ping/c11%3A8080>
3. c10 --> c11: <http://172.1.1.0:8010/ping/c11%3A8080>

## Aufruf mit Servicename: sc11
1. c10 --> c11: <http://localhost:8010/ping/sc11%3A8080>
2. c10 --> c11: <http://172.1.1.1:8080/ping/sc11%3A8080>
3. c10 --> c11: <http://172.1.1.0:8010/ping/sc11%3A8080>

## Aufruf mit Alias: c11.ch
1. c10 --> c11: <http://localhost:8010/ping/c11.ch%3A8080>
2. c10 --> c11: <http://172.1.1.1:8080/ping/c11.ch%3A8080>
3. c10 --> c11: <http://172.1.1.0:8010/ping/c11.ch%3A8080>

## Calls
1. c10 --> c11: <http://localhost:8010/call/http%3A%2F%2Fc11%3A8080%2Fping>
2. c10 --> c11 --> c10: <http://localhost:8010/call/http%3A%2F%2Fc11%3A8080%2Fping%2Fc10%3A8080>

## Zwischen den Netzwerken

funktioniert nur mit Gateway oder via Host

### mit Gateway
1. c20: <http://172.2.2.1:8080/ping>
2. c10 --> c20: <http://localhost:8010/ping/172.2.2.1%3A8080> (direkt --> funktioniert nicht)
3. c10 --> c20: <http://localhost:8010/ping/172.2.2.0%3A8020> (mit Gateway --> funktioniert)

### via Host
1. c10 --> c20: <http://localhost:8010/ping/172.17.0.1%3A8020> (via Host --> funktioniert)

# Netzwerke ohne explizite Gateway und ohne IP-Range

network.sh
    
    #!/bin/sh
    docker network create \
	  --driver=bridge \
	  --subnet=172.28.10.0/24 \
	  pingpong-1

docker-compose.yml

    sc10:
      networks:
        default:
          aliases: 
            - c10.ch

    networks:
      default:
        external:
          name: pingpong-1

oder **nur** in **docker-compose.yml**	  
	  
    sc10:
      networks:
        pingpong-1:
          aliases: 
            - c10.ch
 
    networks:
      pingpong-1:
        name: pingpong-1
        driver: bridge
        ipam:
          driver: default
          config:
            - subnet: 172.28.10.0/24
	  

* --> implizier Gateway 172.28.10.1 resp. 172.28.20.1
* c12: 172.28.10.2:8080 (8012), c13: 172.28.10.3:8080 (8013)
* c22: 172.28.20.2:8080 (8022), c23: 172.28.20.3:8080 (8023)

## Aufrufe im eigenen Netzwerk
1. c12: <http://localhost:8012/ping> --> localhost + gemappter Port
2. c12: <http://172.28.10.2:8080/ping> --> IP-Adresse + interner Port
3. c12: <http://172.28.10.1:8012/ping> --> Gateway + gemappter Port
4. c12: <http://172.17.0.1:8012/ping> --> Host + gemappter Port


1. c13: <http://localhost:8013/ping>
2. c13: <http://172.28.10.3:8080/ping>
3. c13: <http://172.28.10.1:8013/ping>
4. c13: <http://172.17.0.1:8013/ping>


1. c22: <http://localhost:8022/ping>
2. c22: <http://172.28.20.2:8080/ping>
3. c22: <http://172.28.20.1:8022/ping>
4. c22: <http://172.17.0.1:8022/ping>


1. c12 --> c13: <http://localhost:8012/ping/172.28.10.3%3A8080>
2. c12 --> c13: <http://172.28.10.2:8080/ping/172.28.10.3%3A8080>
3. c12 --> c13: <http://172.28.10.1:8012/ping/172.28.10.3%3A8080>
4. c12 --> c13: <http://172.17.0.1:8012/ping/172.28.10.3%3A8080>

## Aufruf mit Containername: c13
1. c12 --> c13: <http://localhost:8012/ping/c13%3A8080>
2. c12 --> c13: <http://172.28.10.2:8080/ping/c13%3A8080>
3. c12 --> c13: <http://172.28.10.1:8012/ping/c13%3A8080>
3. c12 --> c13: <http://172.17.0.1:8012/ping/c13%3A8080>

## Aufruf mit Servicename: sc13
1. c12 --> c13: <http://localhost:8012/ping/sc13%3A8080>
2. c12 --> c13: <http://172.28.10.2:8080/ping/sc13%3A8080>
3. c12 --> c13: <http://172.28.10.1:8012/ping/sc13%3A8080>
4. c12 --> c13: <http://172.17.0.1:8012/ping/sc13%3A8080>

## Aufruf mit Alias: c11.ch

1. c12 --> c13: <http://localhost:8012/ping/c13.ch%3A8080>
2. c12 --> c13: <http://172.28.10.2:8080/ping/c13.ch%3A8080>
3. c12 --> c13: <http://172.28.10.1:8012/ping/c13.ch%3A8080>
4. c12 --> c13: <http://172.17.0.1:8012/ping/c13.ch%3A8080>

## Calls
1. c12 --> c13: <http://localhost:8012/call/http%3A%2F%2Fc13.ch%3A8080%2Fping>
2. c12 --> c13 --> c12: <http://localhost:8012/call/http%3A%2F%2Fc13.ch%3A8080%2Fping%2Fc12.ch%3A8080>

## Zwischen den Netzwerken

funktioniert nur mit Gateway oder via Host

### mit Gateway
2. c22: <http://172.28.20.2:8080/ping>
1. c22: <http://172.28.20.1:8022/ping>
3. c12 --> c22: <http://localhost:8012/ping/172.28.20.2%3A8080> (direkt --> funktioniert nicht)
4. c12 --> c22: <http://localhost:8012/ping/172.28.20.1%3A8022> (mit Gateway --> funktioniert)

### via Host
1. c12 --> c22: <http://localhost:8012/ping/172.17.0.1%3A8022> (via Host --> funktioniert)
