#! /usr/bin/env bash
echo $1
echo $2
echo $3
echo $4
BASEDIR=$(dirname $0)
cd $BASEDIR
export PATH=$PATH:.:..\resources:
java -Xms256m -Xmx1024m -cp ../../XIPApp/bin:../lib/pixelmed.jar:../lib/DicomUtil.jar:../lib/mime-util.jar: XipHostedApp $1 $2 $3 $4
read -p "Press any key to continue..."

