package org.cc.torganizer.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.internal.SessionImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractDbUnitJpaTest {

  protected static EntityManagerFactory entityManagerFactory;
  protected static EntityManager entityManager;
  protected static Connection connection;

  @BeforeClass
  public static void initTestFixture() throws Exception {
    // Get the entity manager for the tests.
    entityManagerFactory = Persistence.createEntityManagerFactory("torganizerTest");
    entityManager = entityManagerFactory.createEntityManager();
    connection = ((SessionImpl) (entityManager.getDelegate())).connection();
  }

  @AfterClass
  public static void closeTestFixture() {
    entityManager.close();
    entityManagerFactory.close();
  }

  public void initDatabase(String testData) throws IOException, DatabaseUnitException, SQLException {
    // Connection aufbauen
    IDatabaseConnection dbunitConn = new DatabaseConnection(connection, "test");
    DatabaseConfig dbConfig = dbunitConn.getConfig();
    dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
    dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());

    // Testdaten laden und [NULL] durch null-Value ersetzen

    URL url = getClass().getClassLoader().getResource(testData);
    InputStream is = url.openStream();
    ReplacementDataSet dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(is));
    dataSet.addReplacementObject("[NULL]", null);
    

    // Daten in Datenbank eintragen
    DatabaseOperation.DELETE_ALL.execute(dbunitConn, dataSet);
    DatabaseOperation.CLEAN_INSERT.execute(dbunitConn, dataSet);
  }
}