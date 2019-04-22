package org.cc.torganizer.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DefaultMetadataHandler;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractDbUnitJpaTest{

  private EntityManagerFactory entityManagerFactory;
  private Connection connection;
  protected EntityManager entityManager;

  @BeforeEach
  public void initTestFixture() {
    // Get the entity manager for the tests.
    entityManagerFactory = Persistence.createEntityManagerFactory("torganizerTest");
    entityManager = entityManagerFactory.createEntityManager();    
    connection = ((SessionImpl) (entityManager.getDelegate())).connection();
    entityManager.getTransaction().begin();
  }

  @AfterEach
  public void closeTestFixture() {
	  entityManager.getTransaction().rollback();
	  entityManager.close();
    entityManagerFactory.close();
  }
  
  public void initDatabase(String testData) throws IOException, DatabaseUnitException, SQLException {
    // Connection aufbauen
    IDatabaseConnection dbunitConn = new DatabaseConnection(connection);
    DatabaseConfig dbConfig = dbunitConn.getConfig();
    dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
    dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new DefaultMetadataHandler());
    dbConfig.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, Boolean.TRUE);

    // Testdaten laden und [NULL] durch null-Value ersetzen
    URL url = getClass().getClassLoader().getResource(testData);
    InputStream is = Objects.requireNonNull(url).openStream();
    ReplacementDataSet dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(is));
    dataSet.addReplacementObject("[NULL]", null);
    

    // Daten in Datenbank eintragen
    DatabaseOperation.DELETE_ALL.execute(dbunitConn, dataSet);
    DatabaseOperation.CLEAN_INSERT.execute(dbunitConn, dataSet);
  }
}