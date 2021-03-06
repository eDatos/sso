-- -----------------------------------------------------------------------------
-- EDATOS-3021 - Pasar a hacer uso del CAS de gobierno del vez del SSO
-- Añadidas nuevas propiedades necesarias para la nueva versión del CAS
-- -----------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('DATA_CONFIGURATIONS'),1,1,'metamac.security.delegated.login_url','FILL_ME');
-- Ejemplo: insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('DATA_CONFIGURATIONS'),1,1,'metamac.security.delegated.login_url','https://demo-cas.arte-consultores.com/login');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('DATA_CONFIGURATIONS'),1,1,'metamac.security.delegated.logout_url','FILL_ME');
-- Ejemplo: insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('DATA_CONFIGURATIONS'),1,1,'metamac.security.delegated.logout_url','https://demo-cas.arte-consultores.com/logout');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('DATA_CONFIGURATIONS'),1,1,'metamac.security.datasource.dialect','org.hibernate.dialect.Oracle10gDialect');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- El server prefix ha de ser una URL accesible al ciudadano
UPDATE TB_DATA_CONFIGURATIONS SET CONF_VALUE = 'FILL_ME' WHERE conf_key = 'metamac.security.cas_server_url_prefix';
-- Ejemplo: UPDATE TB_DATA_CONFIGURATIONS SET CONF_VALUE = 'https://estadisticas.arte-consultores.com/authentication-service-internal' WHERE conf_key = 'metamac.security.cas_server_url_prefix';

COMMIT;