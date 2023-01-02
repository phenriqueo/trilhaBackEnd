package com.trilhaback.pedro.repository;

import com.trilhaback.pedro.domain.Dimension;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DimensionRowMapper implements RowMapper<Dimension> {

    @Override
    public Dimension mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Dimension dimension = new Dimension();
//        dimension.setId(rs.getLong("id"));
//        dimension.setName(rs.getString("name"));
        return null;
    }
}
