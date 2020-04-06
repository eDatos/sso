#!/bin/bash

HOME_PATH=metamac-sso-web
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/edatos-internal/tomcats/edatos-internal01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/metamac/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/log4j2.xml
RESTART=1

if [ "$1" == "--no-restart" ]; then
    RESTART=0
fi

scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" -r etc/deploy deploy@192.168.10.16:$TRANSFER_PATH
scp -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" metamac-sso-web/target/authentication-service-internal-*.war deploy@192.168.10.16:$TRANSFER_PATH/authentication-service-internal.war
ssh -o ProxyCommand="ssh -W %h:%p deploy@estadisticas.arte-consultores.com" deploy@192.168.10.16 <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    . $TRANSFER_PATH/deploy/utilities.sh

    ###
    # AUTHENTICATION-SERVICE-INTERNAL
    ###

    if [ $RESTART -eq 1 ]; then
        sudo service edatos-internal01 stop
        checkPROC "edatos-internal"
    fi

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/authentication-service-internal
    sudo mv $TRANSFER_PATH/authentication-service-internal.war $DEPLOY_TARGET_PATH/authentication-service-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/authentication-service-internal.war -d $DEPLOY_TARGET_PATH/authentication-service-internal
    sudo rm -rf $DEPLOY_TARGET_PATH/authentication-service-internal.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment.xml $DEPLOY_TARGET_PATH/authentication-service-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/log4j2.xml $DEPLOY_TARGET_PATH/authentication-service-internal/$LOGBACK_RELATIVE_PATH_FILE

    if [ $RESTART -eq 1 ]; then
        sudo chown -R edatos-internal.edatos-internal /servers/edatos-internal     
        sudo service edatos-internal01 start
    fi

    echo "Finished deploy"

EOF