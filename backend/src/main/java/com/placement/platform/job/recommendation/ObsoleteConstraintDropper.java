package com.placement.platform.job.recommendation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ObsoleteConstraintDropper implements CommandLineRunner {

    private final DataSource dataSource;

    public ObsoleteConstraintDropper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Checking for obsolete database indexes on job_recommendations table...");
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 1. Get all unique indexes on job_recommendations
            Map<String, List<String>> indexColumns = new HashMap<>();
            try (ResultSet rs = stmt.executeQuery("SHOW INDEX FROM job_recommendations")) {
                while (rs.next()) {
                    boolean nonUnique = rs.getBoolean("Non_unique");
                    String indexName = rs.getString("Key_name");
                    String columnName = rs.getString("Column_name");
                    
                    // Filter unique indexes (except primary key)
                    if (!nonUnique && !"PRIMARY".equalsIgnoreCase(indexName)) {
                        indexColumns.computeIfAbsent(indexName, k -> new ArrayList<>()).add(columnName);
                    }
                }
            }
            
            System.out.println("Found unique indexes: " + indexColumns);

            // 2. Find and drop the unique index on (user_id, job_id) only
            for (Map.Entry<String, List<String>> entry : indexColumns.entrySet()) {
                String indexName = entry.getKey();
                List<String> columns = entry.getValue();
                
                // We want to drop the unique index on user_id, job_id that does not include generation_id
                if (columns.size() == 2 && columns.contains("user_id") && columns.contains("job_id")) {
                    System.out.println("Dropping obsolete unique constraint index: " + indexName + " on columns: " + columns);
                    try {
                        stmt.executeUpdate("ALTER TABLE job_recommendations DROP INDEX " + indexName);
                        System.out.println("Successfully dropped index: " + indexName);
                    } catch (Exception e) {
                        System.err.println("Failed to drop obsolete index " + indexName + ": " + e.getMessage());
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Could not query or alter table index. This is expected if table does not exist or index is already dropped: " + e.getMessage());
        }
    }
}
