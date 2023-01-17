package com.trilhaback.pedro.repository;

import com.trilhaback.pedro.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequestScope
public class DimensionContentJDBCRepository implements DimensionContentRepository {

    private static final String INSERT_DIMENSIONCONTENT = "INSERT INTO dim_{0,number,#}(id, name) VALUES({1}, ''{2}'')";
    private static final String UPDATE_DIMENSIONCONTENT = "UPDATE dim_{0,number,#} SET name = ''{1}'' WHERE id = {2}";
    private static final String FIND_BY_ID = "SELECT id, name FROM dim_{0,number,#} WHERE id = {1}";
    private static final String DELETE_BY_ID = "DELETE FROM dim_{0,number,#} WHERE id = {1}";
    private static final String FIND_ALL = "SELECT id, name FROM dim_{0,number,#}";
    private static final String ADD_DIMENSION_RELATIONSHIP = "UPDATE dim_{0,number,#} SET id_{1,number,#} = {2} WHERE id = {3}";
    private Connection connection;

    public DimensionContentJDBCRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(DimensionContent dimensionContent, Dimension dimension) {
        String insertDimensionContent;
        if (dimension.getDataType() == DataType.INT) {
            insertDimensionContent = MessageFormat.format(INSERT_DIMENSIONCONTENT,
                    dimension.getId(), Integer.parseInt(dimensionContent.getId()), dimensionContent.getName());
        } else {
            insertDimensionContent = MessageFormat.format(INSERT_DIMENSIONCONTENT,
                    dimension.getId(), dimensionContent.getId());
        }
        ;

        try (PreparedStatement pstm = connection.prepareStatement(insertDimensionContent)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DimensionContent update(DimensionContent dimensionContent, Dimension dimension) {
        String updateDimensionContent = MessageFormat.format(UPDATE_DIMENSIONCONTENT,
                dimension.getId(), dimensionContent.getName(), dimensionContent.getId());
        try (PreparedStatement pstm = connection.prepareStatement(updateDimensionContent)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimensionContent;
    }

    @Override
    public DimensionContent findById(Long dimensionId, String dimensionContentId) {
        String findById = MessageFormat.format(FIND_BY_ID, dimensionId, dimensionContentId);
        DimensionContent dimensionContent = new DimensionContent();
        try (PreparedStatement pstm = connection.prepareStatement(findById)) {
            pstm.execute();
            try (ResultSet rs = pstm.getResultSet()) {
                while (rs.next()) {
                    dimensionContent = dimensionContent.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimensionContent;
    }

    @Override
    public List<DimensionContent> findAll(Long dimensionId) {
        List<DimensionContent> dimensionContentList = new ArrayList<>();
        String findAll = MessageFormat.format(FIND_ALL, dimensionId);
        try (PreparedStatement pstm = connection.prepareStatement(findAll)) {
            pstm.execute();
            try (ResultSet rs = pstm.getResultSet()) {
                while (rs.next()) {
                    dimensionContentList.add(DimensionContent.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dimensionContentList;
    }

    @Override
    public void deleteById(Long dimensionId, String dimensionContentId) {
        String deleteById = MessageFormat.format(DELETE_BY_ID, dimensionId, dimensionContentId);
        try (PreparedStatement pstm = connection.prepareStatement(deleteById)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDimensionRelationship(Long dimensionId, DimensionContent dimensionContent) {
        String addDimensionRelationship = MessageFormat
                .format(ADD_DIMENSION_RELATIONSHIP,
                        dimensionId,
                        dimensionContent.getNodeContentList().get(0).getDimensionId(),
                        dimensionContent.getNodeContentList().get(0).getDimensionContentId(),
                        dimensionContent.getId());
        try (PreparedStatement pstm = connection.prepareStatement(addDimensionRelationship)) {
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
