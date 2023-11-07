#!/bin/bash

set -e

export PATH=$PATH:/opt/keycloak/bin
export REALM=yagatalk

function create_user() {
  # params are [username, password, role]
  kcadm.sh create users -r $REALM -s username=$1 -s enabled=true
  kcadm.sh set-password -r $REALM --username=$1 --new-password=$2
  kcadm.sh add-roles -r $REALM --uusername=$1 --rolename $3
  echo "user created $1:$2 role=$3"
}

kcadm.sh config credentials --server http://localhost:8080 --realm master --user admin --password admin

create_user author1 author1 author
create_user author2 author2 author
create_user author3 author3 author
create_user admin admin admin
