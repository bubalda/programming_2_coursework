package com.example.generalfx;

import java.sql.*;
import java.util.*;

/**Some notes:
 * try() is called try-with-resources, helps autoclose autocloseable resources*/

/** An interface for using SQLite in a friendly manner.
 * For all columns operations [*], any other additional input will be ignored (limiter)*/
public class SQLiteFetch {
    private static final String URL = "jdbc:sqlite:shoppingPlatform.db";
    private SQLiteFetch() {}

    private static String joinConditional(Map<String, Object> conditions) {
        StringJoiner s = new StringJoiner(" AND ");

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            Object value = entry.getValue();
            if (entry.getValue() instanceof String) {
                value = "\"" + value + "\"";
            }
            s.add(entry.getKey() + (" = ") + value);
        }
        return s.toString();
    }

    /**Rendered my getColumns and getRows useless, GREAT.
     * Just a combination of both of them, rules apply, returns List of Maps*/
    public static List<Map<String, Object>> getElements(String tableName, List<String> getColumns) {
        return getElements(tableName, getColumns, Map.of());
    }
    public static List<Map<String, Object>> getElements(String tableName, Map<String, Object> conditions) {
        return getElements(tableName, List.of("*"), conditions);
    }
    public static List<Map<String, Object>> getElements(String tableName) {
        return getElements(tableName, List.of("*"), Map.of());
    }
    public static List<Map<String, Object>> getElements(String tableName, List<String> getColumns, Map<String, Object> conditions) {
        String sql = "";
        if (tableName.isEmpty() || getColumns.isEmpty()) return List.of();
        else if (getColumns.contains("*")) sql = "SELECT * FROM " + tableName;
        else sql = "SELECT " + String.join(", ", getColumns) + " FROM " + tableName;

        if (conditions.isEmpty() || conditions.containsKey("*") || conditions.containsValue("*")) sql += ";";
        else sql += " WHERE " + joinConditional(conditions) + ";";

        List<Map<String, Object>> res = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             ResultSet rs = conn.prepareStatement(sql).executeQuery();){

            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                res.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Error getting results: " + e.getMessage());
        }
        return res;
    }


    /**Use 'List.of("*")' or nothing (overload) to query all columns.*/
    public static Map<String, List<Object>> getColumns(String tableName) {
        return getColumns(tableName, List.of("*"));
    }
    public static Map<String, List<Object>> getColumns(String tableName, List<String> getColumns) {
        if (tableName.isEmpty() || getColumns.isEmpty()) return Map.of();

        String sql = getColumns.contains("*")
                ? "SELECT * FROM " + tableName
                : "SELECT " + String.join(", ", getColumns) + " FROM " + tableName;

        Map<String, List<Object>> res = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(URL);
            ResultSet rs = conn.prepareStatement(sql).executeQuery();) {

            ResultSetMetaData md = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                res.put(md.getColumnName(i), new ArrayList<>());
            }

            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    res.get(md.getColumnName(i)).add(rs.getObject(i));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting results: " + e.getMessage());
        }
        return res;
    }

    /**Use "new Map.of("*", "*")" or nothing (overload) to query all columns.*/
    public static List<Map<String, Object>> getRows(String tableName) {
        return getRows(tableName, Map.of("*", "*"));
    }
    public static List<Map<String, Object>> getRows(String tableName, Map<String, Object> conditions) {
        if (tableName.isEmpty() || conditions.isEmpty()) return List.of();
        String sql = conditions.containsKey("*") && conditions.get("*").equals("*")
                ? "SELECT * FROM " + tableName + ";"
                : "SELECT * FROM " + tableName + " WHERE " + joinConditional(conditions) + ";";

        List<Map<String, Object>> res = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             ResultSet rs = conn.prepareStatement(sql).executeQuery();){

            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                res.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Error getting results: " + e.getMessage());
        }
        return res;
    }

    public static boolean addRow(String tableName, HashMap<String, Object> colValues) {
        if (tableName.isEmpty()|| colValues.isEmpty()) return false;

        StringJoiner v = new StringJoiner(", ");
        for (Object val: colValues.values()) {
            if (val instanceof String) {
                v.add("\"" + val + "\"");
            } else {
                v.add(val.toString());
            }
        }

        // Main
        int r = 0;
        String sql = "INSERT INTO " + tableName + " (" + String.join(", ", colValues.keySet()) + ") VALUES (" + v + ");";
        try {
            Connection conn = DriverManager.getConnection(URL);
            r = conn.prepareStatement(sql).executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error inserting query: " + e.getMessage());
        }
        return r > 0;
    }

    /**colValues can be infinitely big when column name is correct, returns True/False for successful,
     * errors will be printed (False doesn't mean error)*/
    public static boolean delRow(String tableName, HashMap<String, Object> colValues) {
        if (tableName.isEmpty() || colValues.isEmpty()) return false;
        int r = 0;
        String sql = "DELETE FROM " + tableName + " WHERE " + joinConditional(colValues) + ";";
        try (Connection conn = DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement(sql);) {
            r = ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error inserting query: " + e.getMessage());
        }

        return r > 0;
    }

    public static void main(String[] args) { // Maybe need singleton for getting its own db? Or just grab col with id == id
//        String[] enc = Encryption.encrypt("Groot");
//        HashMap<String, Object> root = new HashMap<>();
//        root.put("UserName", "Root");
//        root.put("UserPassword", enc[0]);
//        root.put("UserSalt", enc[1]);
//        root.put("UserPermissionLevel", 0);
//
//        enc = Encryption.encrypt("Password");
//        HashMap<String, Object> admin = new HashMap<>();
//        admin.put("UserName", "Admin");
//        admin.put("UserPassword", enc[0]);
//        admin.put("UserSalt", enc[1]);
//        admin.put("UserPermissionLevel", 1);
//
//        boolean r = SQLiteFetch.addRow("UserAuth", root);
//        System.out.println(r);
//
//        boolean a = SQLiteFetch.addRow("UserAuth", admin);
//        System.out.println(a);

        List<Map<String, Object>> r1 = getElements("UserAuth", List.of("*"), Map.of("*", "*"));
        for (Map<String, Object> row : r1) {
            System.out.println(row);
        }
//        Map<String, List<Object>> r2 = getColumns("UserAuth", List.of("*", "UserPassword"));
//        System.out.println(r2);

//        boolean dr = SQLiteFetch.delRow("UserAuth", root);
//        System.out.println(dr);
//
//        boolean da = SQLiteFetch.delRow("UserAuth", admin);
//        System.out.println(da);
    }
}
