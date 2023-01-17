package com.trilhaback.pedro.repository;

import com.trilhaback.pedro.domain.Dimension;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;

@Repository
@RequestScope
public class DimensionDDLRepository {

    private static final String CREATE_DIMENSION_CONTENT_TABLE_DDL = """
            CREATE TABLE DIM_{0,number,#} (
            id {1} PRIMARY KEY NOT NULL,
            name varchar(128)
            )
            """;
    private static final String DROP_DIMENSION_CONTENT_TABLE_DDL = "DROP TABLE DIM_{0,number,#} CASCADE";

    private static final String ALTER_DIMENSION_TABLE_ADD_SONID = """
            ALTER TABLE DIM_{0,number,#}
            ADD COLUMN ID_{1,number,#} {2} DEFAULT -999,
            ADD CONSTRAINT ID_{1,number,#}
            FOREIGN KEY (ID_{1,number,#}) REFERENCES DIM_{1,number,#}(id)
            """;

    private static final String INSERT_NULL_LINE = """
            INSERT INTO dim_{0,number,#}(id, name) VALUES(-999, ''Nulo'')
            """;

    private static final String ALTER_DIMENSION_TABLE_REMOVE_SONID = """
            ALTER TABLE DIM_{0,number,#}
            DROP COLUMN ID_{1,number,#}
            """;

    private Connection connection;

    public DimensionDDLRepository(Connection connection) {
        this.connection = connection;
    }

    public void createDimensionContentTable(Dimension dimension) {
        String createTableDimension = MessageFormat.format(CREATE_DIMENSION_CONTENT_TABLE_DDL, dimension.getId(), dimension.getDataType());
        try (PreparedStatement pstm = connection.prepareStatement(createTableDimension)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropDimensionContentTable(Long id) {
        String dropTable = MessageFormat.format(DROP_DIMENSION_CONTENT_TABLE_DDL, id);
        try (PreparedStatement pstm = connection.prepareStatement(dropTable)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void alterDimensionContentTableSonId(Dimension dimension) {
        String alterDimensionContentTableSonId = MessageFormat
                .format(ALTER_DIMENSION_TABLE_ADD_SONID, dimension.getSonId(), dimension.getId(), dimension.getDataType());
        try (PreparedStatement pstm = connection.prepareStatement(alterDimensionContentTableSonId)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertNullLine(Dimension dimension) {
        String insertNullLine = MessageFormat.format(INSERT_NULL_LINE, dimension.getId());
        try (PreparedStatement pstm = connection.prepareStatement(insertNullLine)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropParentDimensionColumn(Dimension dimension) {
        String alterDimensionTableRemoveSonId = MessageFormat.
                format(ALTER_DIMENSION_TABLE_REMOVE_SONID, dimension.getSonId(), dimension.getId());
        try (PreparedStatement pstm = connection.prepareStatement(alterDimensionTableRemoveSonId)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
