package com.trilhaback.pedro.repository;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.domain.DimensionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

import java.sql.*;
import java.text.MessageFormat;
import java.util.List;

@Repository
@RequestScope
public class DimensionJDBCRepository implements DimensionRepository {

    private static final String CREATE_DIMESION = "INSERT INTO dimension(name) VALUES(?)";
    private static final String CREATE_DIMENSION_TABLE = """
            CREATE TABLE DIM_{0,number,#} (
            id {1} PRIMARY KEY NOT NULL,
            name varchar(128)
            )
            """;

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

     try (PreparedStatement pstm = connection.prepareStatement(CREATE_DIMESION, Statement.RETURN_GENERATED_KEYS)){

         pstm.setString(1, dimension.getName());
         pstm.execute();

         try (ResultSet rs = pstm.getGeneratedKeys()){
             while (rs.next()) {
                 Long id = rs.getLong("id");
                 dimension.setId(id);
             }
         }

     } catch (SQLException e) {
         throw new RuntimeException(e);
     }

     String createTable = MessageFormat.format(CREATE_DIMENSION_TABLE, dimension.getId(), dimension.getDatatype()) ;

     try (PreparedStatement pstm = connection.prepareStatement(createTable)){
         pstm.execute();


     } catch (SQLException e) {
         throw new RuntimeException(e);
     }
        return dimension;
    }

    @Override
    public Dimension update(Dimension dimension) {
        return null;
    }

    @Override
    public Dimension findById(Long id) {
        return null;
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
