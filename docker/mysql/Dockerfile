FROM mysql

ENV MYSQL_ROOT_PASSWORD=root

ADD init.sql /docker-entrypoint-initdb.d

EXPOSE 3306:3306