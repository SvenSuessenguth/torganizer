package org.cc.torganizer.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DefaultMetadataHandler;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractDbUnitJpaTest {

  private EntityManagerFactory entityManagerFactory;
  protected EntityManager entityManager;

  @BeforeEach
  public void initTestFixture() {
    // Get the entity manager for the tests.
    entityManagerFactory = Persistence.createEntityManagerFactory("testPU");
    entityManager = entityManagerFactory.createEntityManager();
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
    Connection connection = entityManager.unwrap(Connection.class);
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