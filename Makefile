#!/bin/bash
OS := $(shell uname)
UID := $(shell id -u)

ifneq ("$(wildcard .env)","")
  $(info ${Y} using .env)
  include .env
else
  $(info ${Y} using .env.example)
  include .env.example
endif

NAMESERVER_IP := $(shell ip address | grep docker0)
DOCKER_APP := php-${APP_NAME}
THIS_PROJECT_DIR := $(dir $(abspath $(firstword $(MAKEFILE_LIST))))
COVERAGE_FOLDER := ./.cover-report

# Y = Yellow e R = Reset
Y := $(shell tput -Txterm setaf 3)
R := $(shell tput -Txterm sgr0)

ifeq ($(OS),Darwin)
	NAMESERVER_IP = host.docker.internal
else ifeq ($(NAMESERVER_IP),)
	NAMESERVER_IP = $(shell grep nameserver /etc/resolv.conf  | cut -d ' ' -f2)
else
	NAMESERVER_IP = 172.17.0.1
endif

stop:
	$(info ${Y} Stopping containers ${R})
	U_ID=${UID} docker-compose stop

up:
	$(info ${Y} Running containers in docker-compose ${R})
	${MAKE} stop
	U_ID=${UID} HOST=${NAMESERVER_IP} docker-compose up -d
	${MAKE} logs

restart:
	$(info ${Y} Restarting container ${R})
	${MAKE} stop
	${MAKE} run

pos-build:
	$(info ${Y} Resolve permiss for artisan ${R})
	U_ID=${UID} docker exec -it --user ${UID} ${DOCKER_APP} chmod +x artisan
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan cache:clear
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan config:clear
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan view:clear
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan route:clear

build:
	$(info ${Y} Building all containers ${R})
	${MAKE} stop
	U_ID=${UID} HOST=${NAMESERVER_IP} docker-compose build
	${MAKE} composer-install

remove-lock-file:
	$(info ${Y} Remove composer.lock ${R})
	rm -rf ./composer.lock

composer-install:
	${MAKE} remove-lock-file
	$(info ${Y} Composer install all dependencies ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php composer install --no-scripts --optimize-autoloader
	$(info ${Y} Dump autoload ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php composer dump-autoload

composer-install-lib:
	$(info ${Y} Intall lib with composer ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php composer require $(filter-out $@,$(MAKECMDGOALS))

execute:
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan $(filter-out $@,$(MAKECMDGOALS))

key-generate:
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan key:generate

composer-install-lib-dev:
	$(info ${Y} Intall lib with composer for dev ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php composer require --dev $(filter-out $@,$(MAKECMDGOALS))

bash:
	$(info ${Y} Enter in bash console of the container ${R})
	U_ID=${UID} docker exec -it --user ${UID} ${DOCKER_APP} bash

bash-root:
	$(info ${Y} Enter in bash console of the container ${R})
	U_ID=${UID} docker exec -it --user root ${DOCKER_APP} bash

test:
	$(info ${Y} Executing tests ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan test $(filter-out $@,$(MAKECMDGOALS)) --testdox

test-class:
	$(info ${Y} Executing tests in Unique class ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php artisan test --filter $(filter-out $@,$(MAKECMDGOALS)) --testdox

cs:
	$(info ${Y} Check for syle code with PSR-2 patten ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./vendor/bin/php-cs-fixer fix src --diff
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./vendor/bin/php-cs-fixer fix tests --diff

install-authentication:
	$(info ${Y} Instaling authentication Sanctum... ${R})
	${MAKE} composer-install-lib laravel/sanctum
	$(info ${Y} Running publish Sanctum Provider ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan vendor:publish --provider="Laravel\Sanctum\SanctumServiceProvider"
	$(info ${Y} Running migrate Sanctum ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan execute migrate

insights:
	$(info ${Y} Check code quality using phpinsights ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./vendor/bin/phpinsights --flush-cache -v

coverage-show-report:
	$(info ${Y} Show Coverage Report in HTML ${R})
	php -S localhost:8888 -t ${COVERAGE_FOLDER}

coverage:
	$(info ${Y} Generate Coverage Report in HTML ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php \
		./artisan test ./tests/ \
		--coverage-html ${COVERAGE_FOLDER} \
		--coverage-clover ${COVERAGE_FOLDER}/clover.xml \
		--log-junit ${COVERAGE_FOLDER}/junit.xml  --testdox
	${MAKE} coverage-show-report

sonar-analyse:
	$(info ${Y} Analyse with Sonar ${R})
	sonar-scanner \
		-Dsonar.projectKey=${SONAR_PROJECT_KEY} \
		-Dsonar.sources=src \
		-Dsonar.tests=tests \
		-Dsonar.language=php \
		-Dsonar.sourceEncoding=UTF-8 \
		-Dsonar.php.coverage.reportPaths=${COVERAGE_FOLDER}/clover.xml \
		-Dsonar.php.tests.reportPath=${COVERAGE_FOLDER}/junit.xml \
		-Dsonar.projectBaseDir=${THIS_PROJECT_DIR} -X \
		-Dsonar.host.url=${SONNAR_HOST_URL} \
		-Dsonar.login=${SONAR_LOGIN_TOKEN}

logs:
	$(info ${Y} Show containers logs ${R})
	docker-compose logs $(filter-out $@,$(MAKECMDGOALS)) --tail="100"

run-queue:
	$(info ${Y} Running queue ${R})
	U_ID=${UID} docker-compose run --rm --user ${UID} php ./artisan queue:work sqs_completed --verbose --tries=3