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