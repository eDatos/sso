-- -----------------------------------------------------------------------------
-- EDATOS-3021 - Pasar a hacer uso del CAS de gobierno del vez del SSO
-- Deprecadas aquellas propiedades innecesarias en la nueva versión del CAS
-- -----------------------------------------------------------------------------

UPDATE TB_DATA_CONFIGURATIONS SET CONF_KEY = concat('deprecated.', CONF_KEY) WHERE conf_key like 'cas%';
UPDATE TB_DATA_CONFIGURATIONS SET CONF_KEY = concat('deprecated.', CONF_KEY) WHERE conf_key = 'server.prefix';
UPDATE TB_DATA_CONFIGURATIONS SET CONF_KEY = concat('deprecated.', CONF_KEY) WHERE conf_key = 'host.name';
UPDATE TB_DATA_CONFIGURATIONS SET CONF_KEY = concat('deprecated.', CONF_KEY) WHERE conf_key = 'database.hibernate.dialect';
UPDATE TB_DATA_CONFIGURATIONS SET CONF_KEY = concat('deprecated.', CONF_KEY) WHERE conf_key like 'metamac.security.ldap%';

COMMIT;