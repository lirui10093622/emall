FROM mysql:latest

COPY ./sql  /tmp/sql
RUN mv /tmp/sql/*.sql /docker-entrypoint-initdb.d
RUN rm -rf /tmp/sql

CMD ["mysqld"]