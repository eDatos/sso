-- ###########################################
-- # insert
-- ###########################################

-- URL LDAP Directory
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.ldap.url','FILL_ME_WITH_LDAP_URL');
-- DN user to execute queries in LDAP Directory
-- Sample: cn=admin,dc=mycompany,dc=com
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.ldap.user_dn','FILL_ME_WITH_DN_USER');
-- Password of user than can execute queries in LDAP Directory
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.ldap.password','FILL_ME_WITH_USER_PASSWORD');
-- Filter to execute queries to restrieve users
-- Sample: uid=%u
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.ldap.filter','FILL_ME_WITH_USER_QUERY');
-- DN base where users are located in LDAP Directory
-- Sample: ou=users,dc=mycompany,dc=com
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.ldap.search_base',null);
        
-- DATASOURCE: ORACLE
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.datasource.url','jdbc:oracle:thin:@FILL_ME_WITH_HOST:FILL_ME_WITH_PORT:XE');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.datasource.username','FILL_ME_WITH_DB_USER');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.datasource.password','FILL_ME_WITH_DB_PASSWORD');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.datasource.driver_name','oracle.jdbc.driver.OracleDriver');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.security.datasource.query.attributes','SELECT usr.username as username, ''ACL'' as acl_name,  app.code || ''#'' || rol.code || ''#'' || operations.code as acl_value
        FROM TB_ACCESS acc
        LEFT JOIN TB_USERS usr ON acc.USER_FK = usr.ID
        LEFT JOIN TB_ROLES rol ON acc.ROLE_FK = rol.ID
        LEFT JOIN TB_APPS app ON acc.APP_FK = app.ID
        LEFT JOIN TB_EXTERNAL_ITEMS operations ON acc.OPERATION_FK = operations.ID
        WHERE REMOVAL_DATE IS NULL AND {0}');
