# wordzle-backend

A simple mock of 'definitely not Wordle'.

This repo contains only the backend. To run the full app, follow the quickstart below.

![game screenshot](https://github.com/SamKelsey/wordzle-backend/blob/master/screenshot.jpg?raw=true)

## Quickstart

1. Clone the [frontend](https://github.com/SamKelsey/wordzle-frontend) repo so both the front and backend repo's are in the same directory. (Eg. `/home/wordle/backend` and `/home/wordle/frontend`).
2. Ensure `spring.redis.host=redis` in the `application.yaml` of the backend repo.
3. Simply, run `docker-compose up` on the backend docker-compose file and visit **Port 5000** :tada:

Have fun!
