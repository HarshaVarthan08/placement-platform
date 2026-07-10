package com.placement.platform.job.recommendation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObsoleteConstraintDropperTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ObsoleteConstraintDropper dropper;

    @Test
    public void testRun_DropsOnlyObsoleteIndex() throws Exception {
        // Setup mock database connection and statements
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SHOW INDEX FROM job_recommendations")).thenReturn(resultSet);

        // We simulate returning index rows:
        // Index 1 (obsolete UK_old on user_id, job_id)
        // Index 2 (current UK_new on user_id, job_id, generation_id)
        when(resultSet.next()).thenReturn(true, true, true, true, true, false);
        
        when(resultSet.getBoolean("Non_unique")).thenReturn(false); // all are unique
        
        when(resultSet.getString("Key_name")).thenReturn(
                "UK_old", "UK_old",
                "UK_new", "UK_new", "UK_new"
        );
        
        when(resultSet.getString("Column_name")).thenReturn(
                "user_id", "job_id",
                "user_id", "job_id", "generation_id"
        );

        // Run
        dropper.run();

        // Verify that ALTER TABLE was executed for UK_old index only
        verify(statement, times(1)).executeUpdate("ALTER TABLE job_recommendations DROP INDEX UK_old");
        verify(statement, never()).executeUpdate("ALTER TABLE job_recommendations DROP INDEX UK_new");
    }

    @Test
    public void testRun_HandlesExceptionGracefully() throws Exception {
        // If an exception is thrown, it should catch it and not propagate
        when(dataSource.getConnection()).thenThrow(new RuntimeException("Database offline"));

        // Run should complete without throwing
        dropper.run();
    }
}
