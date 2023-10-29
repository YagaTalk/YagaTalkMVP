#!/bin/bash

set -e

(cd web-component; rm -rf build; npm install; npm run build)

(cd admin-web-component; rm -rf build; npm install; npm run build)