# docker-basics

Projekt zum Tüfteln von Docker, Docker Compose, etc.

    networks:
      default:
        aliases: 
          - container1.ch

--> network-scoped alias is supported only for containers in user defined networks

## Fazit:
--> eigenes (explizites) Netzwerk verwenden, wenn man mit alias arbeiten möchte.

Man kann aber auch ganz einfach die aliase weglassen und stattdessen den andern Container mit dem Containernamen aufrufen. 
Dies bedingt aber ein eigenes Netzwerk,

# Netzwerke
* 172.1.1.0 Container c10 - 8010, c11 - 8011
* 172.2.2.0 Container c20 - 8020, c21 - 8021

# Aufrufe im eigenen Netzwerk
1. c10: <http://localhost:8010/znueni/manage/ping> --> localhost + gemappter Port
2. c10: <http://172.1.1.0:8080/znueni/manage/ping> --> IP-Adresse + interner Port
3. c10: <http://172.1.1.254:8010/znueni/manage/ping> --> Gateway + gemappter Port


1. c11: <http://localhost:8011/znueni/manage/ping>
2. c11: <http://172.1.1.1:8080/znueni/manage/ping>
3. c11: <http://172.1.1.254:8011/znueni/manage/ping>


1. c20: <http://localhost:8020/znueni/manage/ping>
2. c20: <http://172.2.2.0:8080/znueni/manage/ping>
3. c20: <http://172.2.2.254:8020/znueni/manage/ping>


1. c10 --> c11: <http://localhost:8010/znueni/manage/ping/172.1.1.1%3A8080>
2. c10 --> c11: <http://172.1.1.0:8080/znueni/manage/ping/172.1.1.1%3A8080>
2. c10 --> c11: <http://172.1.1.254:8010/znueni/manage/ping/172.1.1.1%3A8080>

### Aufruf mit Containername: c11
1. c10 --> c11: <http://localhost:8010/znueni/manage/ping/c11%3A8080>
2. c10 --> c11: <http://172.1.1.0:8080/znueni/manage/ping/c11%3A8080>
3. c10 --> c11: <http://172.1.1.254:8010/znueni/manage/ping/c11%3A8080>

### Aufruf mit Servicename: sc11
1. c10 --> c11: <http://localhost:8010/znueni/manage/ping/sc11%3A8080>
2. c10 --> c11: <http://172.1.1.0:8080/znueni/manage/ping/sc11%3A8080>
3. c10 --> c11: <http://172.1.1.254:8010/znueni/manage/ping/sc11%3A8080>

# Calls
1. c10 --> c11: <http://localhost:8010/znueni/manage/call/http%3A%2F%2Fc11%3A8080%2Fznueni%2Fmanage%2Fping>
2. c10 --> c11 --> c10: <http://localhost:8010/znueni/manage/call/http%3A%2F%2Fc11%3A8080%2Fznueni%2Fmanage%2Fping%2Fc10%3A8080>

# Zwischen den Netzwerken funktioniert's nur mit dem Gateway
1. c20: <http://172.2.2.0:8080/znueni/manage/ping>
2. c10 --> c20: <http://localhost:8010/znueni/manage/ping/172.2.2.0%3A8080> (direkt --> funktioniert nicht)
3. c10 --> c20: <http://localhost:8010/znueni/manage/ping/172.2.2.254%3A8020> (mit Gateway --> funktioniert)

