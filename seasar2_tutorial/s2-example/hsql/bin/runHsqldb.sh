#!/bin/sh
BASE_DIR=`dirname $0`
cd $BASE_DIR/../data
java -classpath ../lib/hsqldb-1.8.0.1.jar org.hsqldb.Server
