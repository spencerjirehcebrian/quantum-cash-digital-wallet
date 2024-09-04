FROM postgres:13

RUN apt-get update && apt-get install -y vim

EXPOSE 5432

CMD ["postgres"]
