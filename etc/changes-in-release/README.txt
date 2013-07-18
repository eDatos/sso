Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. DATA

	- Refactor de propiedades
		- metamac.security.casServiceLoginUrl por metamac.security.cas_service_login_url
		- metamac.security.casServiceLogoutUrl por metamac.security.cas_service_logout_url
		- metamac.security.casServerUrlPrefix por metamac.security.cas_server_url_prefix

99. Reiniciar Tomcat