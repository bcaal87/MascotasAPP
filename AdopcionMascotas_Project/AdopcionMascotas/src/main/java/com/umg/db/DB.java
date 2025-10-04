package com.umg.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    // Usa SOLO una de las siguientes URLs (comenta la otra):
    // Instancia por defecto (MSSQLSERVER):
    // private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DemoMascotas;encrypt=true;trustServerCertificate=true";

    // SQL Express (SQLEXPRESS) con puerto 1433:
    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=DemoMascotas;encrypt=true;trustServerCertificate=true";

    private static final String USER = "sa";         // Cambiar
    private static final String PASS = "TuPassword"; // Cambiar

    public static Connection get() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
