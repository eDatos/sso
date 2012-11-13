Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Añadidas propiedades en el DATA (cas.xml):
	- metamac.security.datasource.driverName: para especificar el driver de la base de datos que se esté utilizando 
	- metamac.security.datasource.query.attributes: para especificar la query para obtener las acls de la base de datos de metamac-access-control

99. Reiniciar Tomcat