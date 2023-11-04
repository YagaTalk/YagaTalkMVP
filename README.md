# YagaTalkMVP

## Local environment start

```
cd devenv 

docker-compose up -d

# wait for keycloak to start

docker compose exec keycloak bash /opt/keycloak/bin/create-users.sh
```
