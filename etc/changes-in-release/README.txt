Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Cambios en BBDD: Ver subcarpeta /db
	- Es necesario ejecutar los scripts de inserts de la configuración del DATA
	
3. Cambios en data: Ver subcarpeta /conf
	- Desaparece el directorio DATA al completo
	- Las propiedades del DATA pasan a estar en BBDD.


99. Reiniciar Tomcat