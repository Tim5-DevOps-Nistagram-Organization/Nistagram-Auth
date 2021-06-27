#!/bin/sh

./consul agent -config-dir=/consul-config &

java -jar nistagram-auth.jar