#!/bin/bash

export DOMAIN_NAME=51.250.96.167

sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout ~/yagatalk/nginx-cert/$DOMAIN_NAME.key \
  -out ~/yagatalk/nginx-cert/$DOMAIN_NAME.crt