package com.m7.abs.common.handler;

import com.m7.abs.common.utils.FastJsonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 重写JsonbTypeHandler  支持jsonb
 */
@MappedTypes({Object.class})
public class JsonbTypeHandler extends BaseTypeHandler<Object> {

    private static final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        if (preparedStatement != null) {
            jsonObject.setType("jsonb");
            jsonObject.setValue(FastJsonUtils.toJSONString(o));
            preparedStatement.setObject(i, jsonObject);
        }
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.getObject(s);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getObject(i);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getObject(i);
    }
}
