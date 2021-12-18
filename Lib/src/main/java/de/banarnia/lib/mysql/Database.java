package de.banarnia.lib.mysql;

import de.banarnia.lib.util.AbstractFileLoader;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/* Database-Klasse
 * Beinhaltet alle Informationen und Methoden über eine Datenbankverbindung.
 */
public class Database {

    // Verbindungs-Informationen
    private final String    host;
    private final int       port;
    private final String    database;
    private final String    username;
    private final String    password;
    private boolean         useSSL;
    private boolean         autoReconnect = true;

    // Instanz des Loggers
    private Logger logger;

    // Instanz der Connection
    private Connection      connection;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Konstruktor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Konstruktor mit Angabe der Informationen und einem Logger ohne zusätzliche Optionen
    public Database(String host, int port, String database, String username, String password, Logger logger) {
        // Informationen zur Connection
        this.host           = host;
        this.port           = port;
        this.database       = database;
        this.username       = username;
        this.password       = password;

        // Instanz des Loggers
        this.logger = logger;
    }

    // Konstruktor mit Angabe der Informationen zur Connection und einem Logger
    public Database(String host, int port, String database, String username, String password,
                    boolean useSSL, boolean autoReconnect, Logger logger) {
        // Informationen zur Connection
        this.host           = host;
        this.port           = port;
        this.database       = database;
        this.username       = username;
        this.password       = password;
        this.useSSL         = useSSL;
        this.autoReconnect  = autoReconnect;

        // Instanz des Loggers
        this.logger = logger;
    }

    // Konstruktor mit einer Config-Datei und einem Logger
    public Database(AbstractFileLoader config, String section, Logger logger) {
        // Null check
        if (config == null || section == null)
            throw new NullPointerException();

        // Werte aus Config auslesen
        this.host = config.getOrElseSet(section + ".Host", "127.0.0.1");
        this.port = config.getOrElseSet(section + ".Port", 3306);
        this.database = config.getOrElseSet(section + ".Database", "Database-Name");
        this.username = config.getOrElseSet(section + ".Username", "Username");
        this.password = config.getOrElseSet(section + ".Password", "Password");
        this.useSSL = config.getBoolean(section + ".Use-SSL", false);
        this.autoReconnect = config.getBoolean(section + ".Auto-Reconnect", true);

        // Instanz des Loggers
        this.logger = logger;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Connection-Methoden ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Abfrage, ob die Verbindung hergestellt ist
    public final boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.warning("Error occured while testing the database connection to database: " + database);
            return false;
        }
    }

    // Connection öffnen
    public final boolean openConnection() {
        // Abfrage, ob die Connection schon hergestellt wurde
        if (isConnected()) return true;

        // Abfrage, ob der Treiber installiert ist
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            logger.warning("The MySQL connection could not be established because the jdbc driver is not installed");
            return false;
        }

        // Properties zur Verbindung zur Datenbank
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("useSSL", "false");
        properties.setProperty("autoReconnect", "true");

        // Versuchen die Verbindung herzustellen
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.warning("Error occured while connecting to the database: " + database);
            return false;
        }

        // True zurückgeben, wenn kein Fehler aufgetreten ist
        return true;
    }

    // Connection schließen
    public void closeConnection() {
        // Abfrage, ob die Verbindung hergestellt ist
        if (!isConnected()) return;

        // Verbindung schließen
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.warning("Error occured while trying to close the connection to the database: " + database);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Datenbank-NullCheck ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Null check
    private final void nullCheck(Object... objects) {
        // Abfrage, ob die Connection != null ist
        if (connection == null)
            throw new NullPointerException();

        // Abfrage, ob die Argumente != null sind
        for (Object object : objects)
            if (object == null)
                throw new NullPointerException();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Datenbank-Update ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // MySQL Update
    public final boolean executeUpdate(String query) {
        return executeUpdate(query, null);
    }

    // MySQL Update - mit Argumenten
    public final boolean executeUpdate(String query, Object... args) {
        // Null checks
        nullCheck(query);

        // PreparedStatement anlegen -> effizienter als normale Statement
        PreparedStatement statement = null;

        // String für Log-Nachricht
        String arguments = args.length != 0 ? " with " + args.length + " parameters" : null;

        // Try-Catch für SQL Methoden
        try {
            // Statement anlegen
            statement = connection.prepareStatement(query);

            // Objects in die Parameter des Query-String einfügen -> ersetzt '?' durch das Object
            for (int i = 0; i < args.length; i++)
                statement.setObject(i + 1, args[i]);

            // Update durchführen
            statement.executeUpdate();

        // Fehler handling
        } catch (SQLException ex) {
            // Error printen
            ex.printStackTrace();

            // Log-Info, ggf. mit der Anzahl der Parameter
            logger.warning("Failed to send a MySQL Query" +
                    arguments +
                    ":");
            logger.warning(query);

        // Statement closen, wenn abgeschlossen
        } finally {
            // Nur schließen, wenn != null
            try {
                // Statement schließen, wenn != null - sonst False zurückgeben
                if (statement != null)
                    statement.close();
                else
                    return false;

                // True zurückgeben
                return true;

            } catch (SQLException e) {
                // Error printen
                e.printStackTrace();

                // Log-Info, ggf. mit der Anzahl der Parameter
                logger.warning("Failed to close MySQL Statement" +
                        arguments +
                        ":");
                logger.warning(query);

                // False zurückgeben
                return false;
            }
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Datenbank-Query ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // MySQL Abfrage
    public final ResultSet executeQuery(String query) {
        return executeQuery(query, null);
    }

    // MySQL Abfrage - mit Argumenten
    public final ResultSet executeQuery(String query, Object... args) {
        // Null checks
        nullCheck(query);

        // PreparedStatement anlegen -> effizienter als normale Statement
        PreparedStatement statement = null;

        // String für Log-Nachricht
        String arguments = args.length != 0 ? " with " + args.length + " parameters" : null;

        // Try-Catch für SQL Methoden
        try {
            // Statement anlegen
            statement = connection.prepareStatement(query);

            // ResultSet anlegen
            ResultSet resultSet = null;

            // Objects in die Parameter des Query-String einfügen -> ersetzt '?' durch das Object
            for (int i = 0; i < args.length; i++)
                statement.setObject(i + 1, args[i]);

            // Query durchführen
            resultSet = statement.executeQuery();

            // ResultSet zurückgeben
            return resultSet;

            // Fehler handling
        } catch (SQLException ex) {
            // Error printen
            ex.printStackTrace();

            // Log-Info, ggf. mit der Anzahl der Parameter
            logger.warning("Failed to send a MySQL Query" +
                    arguments +
                    ":");
            logger.warning(query);

            // Null zurückgeben
            return null;
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Allgemeine Datenbank Funktionen ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Tabelle löschen
    public final boolean deleteTable(String table) {
        return executeUpdate("DROP TABLE '" + table + "';");
    }

    // Tabelle leeren
    public final boolean clearTable(String table) {
        return executeUpdate("TRUNCATE TABLE '" + table + "';");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getter & Setter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public Connection getConnection() {
        return connection;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
