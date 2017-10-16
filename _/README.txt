Allgemein

Wildfly
	I created folder wildfly.x.x.x/modules/system/layers/base/com/mysql/main I've got here jdbc jar file and module.xml

    <module xmlns="urn:jboss:module:1.3" name="com.mysql">
      <resources>
        <resource-root path="mysql-connector-java-5.1.44-bin.jar"/>
      </resources>
      <dependencies>
        <module name="javax.api"/>
      </dependencies>
    </module>

	Your datasources subsystem should look something like this:

    <subsystem xmlns="urn:jboss:domain:datasources:2.0">
        <datasources>
		...
            <datasource jndi-name="java:/jdbc/torganizerDS" pool-name="torganizerDS" enabled="true" use-java-context="true">
                <connection-url>jdbc:mysql://localhost:3306/torganizer</connection-url>
                <driver>mysql</driver>
                <security>
                    <user-name>root</user-name>
                    <password>root</password>
                </security>
            </datasource>
			
            <drivers>
                ...
                <driver name="mysql" module="com.mysql">
                    <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
                </driver>
            </drivers>
        </datasources>
    </subsystem>
	
	ausführen von ${WILDFLY_HOME}\bin\add-user mit user 'admin'
	
MySQL
	my.ini nach c:\Windows kopieren
	mysql-5.7.19-winx64 nach c:\dev\server kopieren
	c:\dev\server\mysql-5.7.19-winx64\data anlegen
	
	mysqld --initialize-insecure
	mysqld --console

	Datenbank 'test' und 'torganizer' anlegen
