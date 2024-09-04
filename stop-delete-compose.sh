docker compose down

docker volume rm $(docker volume ls -q)
docker compose up