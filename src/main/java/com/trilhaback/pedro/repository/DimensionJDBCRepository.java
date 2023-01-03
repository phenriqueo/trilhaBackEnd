package com.trilhaback.pedro.repository;

import com.trilhaback.pedro.domain.DataType;
import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.domain.DimensionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.text.MessageFormat;
import java.util.List;

@Repository
@RequestScope
public class DimensionJDBCRepository implements DimensionRepository {

    private static final String CREATE_DIMESION = "INSERT INTO dimension(name, data_type) VALUES(?, ?)";
    private static final String CREATE_DIMENSION_TABLE = """
            CREATE TABLE DIM_{0,number,#} (
            id {1} PRIMARY KEY NOT NULL,
            name varchar(128)
            )
            """;
    private static final String UPDATE_DIMENSION = "UPDATE dimension SET name = ?, data_type = ?  WHERE id = ?";
    private static final String FINDBYID ="SELECT id, name, data_type FROM dimension WHERE id = ?";
//    private final JdbcTemplate jdbcTemplate;
    private Connection connection;
//    public DimensionJDBCRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
    public DimensionJDBCRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Dimension insert(Dimension dimension) {
        try (PreparedStatement pstm = connection.prepareStatement(CREATE_DIMESION, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, dimension.getName());
            pstm.setString(2, dimension.getDatatype().toString());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            Long id = rs.getLong("id");
            dimension.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String createTable = MessageFormat.format(CREATE_DIMENSION_TABLE, dimension.getId(), dimension.getDatatype());
        try (PreparedStatement pstm = connection.prepareStatement(createTable)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimension;
    }

    @Override
    public Dimension update(Dimension dimension) {
        try (PreparedStatement pstm = connection.prepareStatement(UPDATE_DIMENSION, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, dimension.getName());
            pstm.setString(2, dimension.getDatatype().toString());
            pstm.setLong(3, dimension.getId());
            pstm.execute();
            try (ResultSet rs = pstm.getGeneratedKeys()){
                while (rs.next()) {
                    String name = rs.getString("name");
                    DataType dataType = DataType.valueOf(rs.getString("data_type"));
                    dimension.setName(name);
                    dimension.setDatatype(dataType);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimension;
    }

    @Override
    public Dimension findById(Long id) {
        Dimension dimension = null;
        try (PreparedStatement pstm = connection.prepareStatement(FINDBYID, Statement.RETURN_GENERATED_KEYS)) {
                pstm.setLong(1, id);
                pstm.execute();
                try (ResultSet rs = pstm.getResultSet()) {
                    while ( rs.next()) {
                        dimension = dimension.builder()
                                .id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .dataType(DataType.valueOf(rs.getString("data_type")))
                                .build();
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimension;
    }
        @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public Dimension findByName(String name) {
        return null;
    }

    @Override
    public List<Dimension> findAll() {
        return null;
    }
}
