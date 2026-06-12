package dao;

import java.sql.*;
import java.util.*;

public class PopupDAO {

    public static List<Map<String, String>> getDetailData(String detailQuery) {
        List<Map<String, String>> results = new ArrayList<>();

        try (
            Connection con = AlertDAO.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(detailQuery);
        ) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    String colName = meta.getColumnName(i);
                    String value = rs.getString(i);
                    row.put(colName, value != null ? value : "");
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}