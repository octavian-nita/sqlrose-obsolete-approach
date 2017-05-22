package eu.sqlrose.sample.db;

import org.h2.tools.RunScript;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 05, 2016
 */
public class CreateH2SampleDb {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.exit(1);
        }

        String dbName = System.getProperty("db.name", "sakila").trim();
        if (dbName.length() == 0) {
            dbName = "sakila";
        }

        String dbMode = System.getProperty("db.mode", "").trim();
        if (dbMode.length() == 0 && dbName.equalsIgnoreCase("sakila")) {
            dbMode = "MySQL";
        }

        String dbFilePrefix = "./sample-db/" + dbName + "/h2";
        Files.deleteIfExists(Paths.get(dbFilePrefix + ".mv.db"));

        String url = "jdbc:h2:" + dbFilePrefix;
        if (dbMode.length() > 0) {
            url += ";MODE=" + dbMode;
        }

        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager.getConnection(url)) {

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            }

            for (String fileName : args) {
                try (Reader script = new BufferedReader(new FileReader(fileName))) {
                    //noinspection unused
                    try (ResultSet rset = RunScript.execute(conn, script)) {
                        System.out.printf("Done executing %s.%n", fileName);
                    }
                } catch (Throwable throwable) {
                    System.err.printf("Cannot execute script %s:%n", fileName);
                    throwable.printStackTrace();
                    System.err.println("Skipping...");
                }
            }
        }
    }
}
