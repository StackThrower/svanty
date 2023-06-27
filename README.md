# Getting Started


### Docker
* docker-compose up --build -d app_svanty8090
* docker-compose stop app_svanty8080
* docker rm -f $(docker ps -a -q)
* docker rmi -f $(docker images -aq)
* docker logs app_app_1
* docker run --name test-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=toor -e MYSQL_USER=svanty -e MYSQL_PASSWORD=toor  -e MYSQL_DATABASE=svanty -d mysql:8.0.27  --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
* docker container ls      (list of all running containers)

### Ngnix
* sudo apt install nginx
* ln -s /etc/nginx/sites-available/svanty.com.conf /etc/nginx/sites-enabled/svanty.com
* sudo systemctl restart nginx

* /etc/nginx/sites-available

* apt-get update
* sudo apt-get install certbot
* apt-get install python3-certbot-nginx   

* sudo certbot --nginx -d svanty.com
* crontab -e
* 0 12 * * * /usr/bin/certbot renew --quiet


### Mysql
* wget https://dev.mysql.com/get/mysql-apt-config_0.8.20-1_all.deb
* sudo dpkg -i mysql-apt-config_0.8.20-1_all.deb
* sudo apt update
* sudo apt install -y mysql-community-server
* mysql -u root -p
* CREATE DATABASE svanty CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
* DROP DATABASE svanty;

* mysqldump -u root -p svanty --no-create-info --single-transaction --extended-insert=FALSE > dump_v12_23.01.22.sql

* CREATE USER 'svanty'@'%' IDENTIFIED BY 'password';
* GRANT ALL ON svanty.* to 'svanty'@'%';
