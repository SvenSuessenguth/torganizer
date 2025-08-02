package org.cc.torganizer.persistence;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DefaultMetadataHandler;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

@Testcontainers
public abstract class AbstractDbUnitJpaTest {

  // dynamic port for Postgresql-Testcontainer to run tests in parallel on Jenkins
  private static final Integer postgresqlPort;

  static {
    var portProp = System.getProperty("postgresql.port");
    postgresqlPort = portProp.isEmpty() ? 5432 : Integer.parseInt(portProp);
  }

  private EntityManagerFactory entityManagerFactory;
  protected EntityManager entityManager;

  // https://stackoverflow.com/questions/69132686/how-can-i-set-the-port-for-postgresql-when-using-testcontainers
  @SuppressWarnings("all")
  @Container
  public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.5")
    .withDatabaseName("torganizer")
    .withUsername("postgres")
    .withPassword("postgres")
    .withExposedPorts(Integer.valueOf(postgresqlPort))
    .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
      new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(postgresqlPort), new ExposedPort(5432)))
    ));

  @BeforeEach
  public void initTestFixture() {
    // overriding defaults from persistence.xml
    // see https://www.generacodice.com/en/articolo/1290009/Read-Environment-Variables-in-persistence.xml-file
    var configOverrides = new HashMap<String, Object>();
    configOverrides.put("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:" + postgresqlPort + "/torganizer");

    // Get the entity manager for the tests.
    entityManagerFactory = Persistence.createEntityManagerFactory("testcontainersPU", configOverrides);
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
    var connection = entityManager.unwrap(Connection.class);
    var dbunitConn = new DatabaseConnection(connection);
    var dbConfig = dbunitConn.getConfig();
    dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
    dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new DefaultMetadataHandler());
    dbConfig.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, Boolean.TRUE);

    // Testdaten laden und [NULL] durch null-Value ersetzen
    var url = getClass().getClassLoader().getResource(testData);
    var is = Objects.requireNonNull(url).openStream();
    var dataSet = new ReplacementDataSet(new FlatXmlDataSetBuilder().build(is));
    dataSet.addReplacementObject("[NULL]", null);

    // Daten in Datenbank eintragen
    DatabaseOperation.DELETE_ALL.execute(dbunitConn, dataSet);
    DatabaseOperation.CLEAN_INSERT.execute(dbunitConn, dataSet);
  }
}