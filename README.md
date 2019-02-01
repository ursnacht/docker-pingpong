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

http://localhost:8081/znueni/manage/ping
http://172.28.5.0:8080/znueni/manage/ping
http://172.28.5.254:8081/znueni/manage/ping
http://localhost:8081/znueni/manage/ping/172.28.5.1%3A8080
http://localhost:8081/znueni/manage/ping/c2%3A8080
http://localhost:8081/znueni/manage/ping/sc2%3A8080
http://localhost:8081/znueni/manage/ping/172.28.5.254%3A8082

http://localhost:8081/znueni/manage/call/http%3A%2F%2Fc2%3A8080%2Fznueni%2Fmanage%2Fping

http://localhost:8081/znueni/manage/call/http%3A%2F%2Fc2%3A8080%2Fznueni%2Fmanage%2Fping%2Fc1%3A8080
