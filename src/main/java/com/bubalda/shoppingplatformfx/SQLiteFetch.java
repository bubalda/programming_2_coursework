package com.bubalda.shoppingplatformfx;

import java.sql.*;
import java.util.*;

/*Some notes: try() is called try-with-resources, helps autoclose autocloseable resources*/
public class SQLiteFetch {
    private static final String URL = "jdbc:sqlite:shoppingPlatform.db";
    private SQLiteFetch() {}

    public static List<LinkedHashMap<String, Object>> arbitraryGet(String sql) {
        List<LinkedHashMap<String, Object>> res = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             ResultSet rs = conn.prepareStatement(sql).executeQuery();){

            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                LinkedHashMap<String, Object> row = new LinkedHashMap<>();
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

    public static boolean arbitraryEdit(String sql) {
        int r = 0;
        try (Connection conn = DriverManager.getConnection(URL);) {
            r = conn.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting query: " + e.getMessage());
        }
        return r > 0;
    }

    private static String joinConditional(Map<String, Object> conditions, boolean useRegex) {
        StringJoiner s = new StringJoiner(" AND ");

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            Object value = entry.getValue();
            if (entry.getValue() instanceof String) {
                value = "\"" + value + "\"";
            }
            s.add(entry.getKey() + (useRegex ? " LIKE ": " = ") + value);
        }
        return s.toString();
    }

    private static String joinUpdateSets(Map<String, Object> conditions) {
        StringJoiner s = new StringJoiner(", ");

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            Object value = entry.getValue();
            if (entry.getValue() instanceof String) {
                value = "\"" + value + "\"";
            }
            s.add(entry.getKey() + " = " + value);
        }
        return s.toString();
    }

    /**Just a combination of both of them, rules apply, returns List of Maps
     * Uses LinkedHashMap to retain order*/
    public static List<LinkedHashMap<String, Object>> getElements(String tableName, List<String> getColumns, boolean useRegex) {
        return getElements(tableName, getColumns, Map.of(), useRegex);
    }
    public static List<LinkedHashMap<String, Object>> getElements(String tableName, Map<String, Object> conditions, boolean useRegex) {
        return getElements(tableName, List.of("*"), conditions, useRegex);
    }
    public static List<LinkedHashMap<String, Object>> getElements(String tableName) {
        return getElements(tableName, List.of("*"), Map.of(), false);
    }
    public static List<LinkedHashMap<String, Object>> getElements(String tableName, List<String> getColumns, Map<String, Object> conditions, boolean useRegex) {
        String sql = "";
        if (tableName.isEmpty() || getColumns.isEmpty()) return List.of();
        else if (getColumns.contains("*")) sql = "SELECT * FROM " + tableName;
        else sql = "SELECT " + String.join(", ", getColumns) + " FROM " + tableName;

        if (conditions.isEmpty() || conditions.containsKey("*") || conditions.containsValue("*")) sql += ";";
        else sql += " WHERE " + joinConditional(conditions, useRegex) + ";";

        return arbitraryGet(sql);
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

        String sql = "INSERT INTO " + tableName + " (" + String.join(", ", colValues.keySet()) + ") VALUES (" + v + ");";
        return arbitraryEdit(sql);
    }

    /**colValues can be infinitely big when column name is correct, returns True/False for successful,
     * errors will be printed (False doesn't mean error)*/
    public static boolean delRow(String tableName, HashMap<String, Object> colValues, boolean useRegex) {
        if (tableName.isEmpty() || colValues.isEmpty()) return false;
        String sql = "DELETE FROM " + tableName + " WHERE " + joinConditional(colValues, useRegex) + ";";
        return arbitraryEdit(sql);
    }

    public static boolean updateRow(String tableName, HashMap<String, Object> colValues, HashMap<String, Object> conditions, boolean useRegex) {
        if (tableName.isEmpty() || colValues.isEmpty()) return false;
        String sql = "UPDATE " + tableName + " SET " + joinUpdateSets(colValues) + " WHERE " + joinConditional(conditions, useRegex) + ";";
        return arbitraryEdit(sql);
    }
}
