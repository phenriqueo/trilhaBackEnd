package com.trilhaback.pedro.repository;

import com.trilhaback.pedro.domain.DataType;
import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.domain.DimensionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequestScope
public class DimensionJDBCRepository implements DimensionRepository {

    private static final String CREATE_DIMESION = "INSERT INTO dimension(name, data_type, sonid) VALUES(?, ?, null)";
    private static final String UPDATE_DIMENSION = "UPDATE dimension SET name = ?, data_type = ?  WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT id, name, data_type, sonid FROM dimension WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT id, name, data_type, sonid FROM dimension WHERE name = ?";
    private static final String DELETE_BY_ID = "DELETE FROM dimension WHERE id = ?";
    private static final String ADD_DIMENSION_PARENT = "UPDATE dimension SET sonid = ? WHERE id = ?";
    private static final String FIND_TREE_BY_ID = "SELECT id FROM dimension WHERE sonid = ?";
    private static final String REMOVE_SON_ID = "UPDATE dimension SET sonid = null WHERE id = ?";

    private Connection connection;

    public DimensionJDBCRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Dimension insert(Dimension dimension) {
        try (PreparedStatement pstm = connection.prepareStatement(CREATE_DIMESION, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, dimension.getName());
            pstm.setString(2, dimension.getDataType().toString());
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            Long id = rs.getLong("id");
            dimension.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimension;
    }

    @Override
    public Dimension update(Dimension dimension) {
        try (PreparedStatement pstm = connection.prepareStatement(UPDATE_DIMENSION, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, dimension.getName());
            pstm.setString(2, dimension.getDataType().toString());
            pstm.setLong(3, dimension.getId());
            pstm.execute();
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    DataType dataType = DataType.valueOf(rs.getString("data_type"));
                    dimension.setName(name);
                    dimension.setDataType(dataType);
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
        try (PreparedStatement pstm = connection.prepareStatement(FIND_BY_ID, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setLong(1, id);
            pstm.execute();
            try (ResultSet rs = pstm.getResultSet()) {
                while (rs.next()) {
                    dimension = dimension.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .dataType(DataType.valueOf(rs.getString("data_type")))
                            .sonId(rs.getLong("sonid"))
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
        int rowNum;
        try (PreparedStatement pstm = connection.prepareStatement(DELETE_BY_ID)) {
            pstm.setLong(1, id);
            rowNum = pstm.executeUpdate();
            ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowNum;
    }

    @Override
    public Dimension findByName(String name) {
        Dimension dimension = null;
        try (PreparedStatement pstm = connection.prepareStatement(FIND_BY_NAME, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, name);
            pstm.execute();
            try (ResultSet rs = pstm.getResultSet()) {
                while (rs.next()) {
                    dimension = dimension.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .dataType(DataType.valueOf(rs.getString("data_type")))
                            .sonId(rs.getLong("sonid"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimension;
    }

    @Override
    public List<Dimension> findAll() {
        List<Dimension> dimensionList = new ArrayList<>();
        try (PreparedStatement pstm = connection.prepareStatement("SELECT name, id, data_type, sonid FROM DIMENSION")) {
            pstm.execute();
            try (ResultSet rs = pstm.getResultSet()) {
                while (rs.next()) {
                    dimensionList.add(Dimension.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .dataType(DataType.valueOf(rs.getString("data_type")))
                            .sonId(rs.getLong("sonid"))
                            .build());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimensionList;
    }

    public void addDimensionSon(Dimension dimension) {
        try (PreparedStatement pstm = connection.prepareStatement(ADD_DIMENSION_PARENT)) {
            pstm.setLong(1, dimension.getSonId());
            pstm.setLong(2, dimension.getId());
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dimension findTreeById(Long id) {
        Dimension dimension = findById(id);
        List<Dimension> parent = new ArrayList<>();
        try (PreparedStatement pstm = connection.prepareStatement(FIND_TREE_BY_ID)) {
            pstm.setLong(1, id);
            pstm.execute();
            try (ResultSet rs = pstm.getResultSet()) {
                while (rs.next()) {
                    parent.add(findTreeById(rs.getLong("id")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dimension.setParent(parent);
        return dimension;
    }

    public void removeSonId(Long id) {
        try (PreparedStatement pstm = connection.prepareStatement(REMOVE_SON_ID)) {
            pstm.setLong(1, id);
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
