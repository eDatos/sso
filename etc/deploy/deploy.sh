#!/bin/sh

HOME_PATH=metamac-sso-web
TRANSFER_PATH=$HOME_PATH/tmp
DEPLOY_TARGET_PATH=/servers/metamac/tomcats/metamac01/webapps
ENVIRONMENT_RELATIVE_PATH_FILE=WEB-INF/classes/metamac/environment.xml
LOGBACK_RELATIVE_PATH_FILE=WEB-INF/classes/log4j.xml

scp -r etc/deploy deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH
scp metamac-sso-web/target/authentication-service-internal-*.war deploy@estadisticas.arte-consultores.com:$TRANSFER_PATH/authentication-service-internal.war
ssh deploy@estadisticas.arte-consultores.com <<EOF

    chmod a+x $TRANSFER_PATH/deploy/*.sh;
    . $TRANSFER_PATH/deploy/utilities.sh

    sudo service metamac01 stop
    checkPROC "metamac"


    ###
    # AUTHENTICATION-SERVICE-INTERNAL
    ###

    # Update Process
    sudo rm -rf $DEPLOY_TARGET_PATH/authentication-service-internal
    sudo mv $TRANSFER_PATH/authentication-service-internal.war $DEPLOY_TARGET_PATH/authentication-service-internal.war
    sudo unzip $DEPLOY_TARGET_PATH/authentication-service-internal.war -d $DEPLOY_TARGET_PATH/authentication-service-internal
    sudo rm -rf $DEPLOY_TARGET_PATH/authentication-service-internal.war

    # Restore Configuration
    sudo cp $HOME_PATH/environment.xml $DEPLOY_TARGET_PATH/authentication-service-internal/$ENVIRONMENT_RELATIVE_PATH_FILE
    sudo cp $HOME_PATH/log4j.xml $DEPLOY_TARGET_PATH/authentication-service-internal/$LOGBACK_RELATIVE_PATH_FILE

    sudo chown -R metamac.metamac /servers/metamac
    sudo service metamac01 start
    checkURL "http://estadisticas.arte-consultores.com/authentication-service-internal" "metamac01"

EOF