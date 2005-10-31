#!/bin/sh
BASE_DIR=`dirname $0`
cd $BASE_DIR/../data
java -classpath ../lib/hsqldb.jar org.hsqldb.Server
