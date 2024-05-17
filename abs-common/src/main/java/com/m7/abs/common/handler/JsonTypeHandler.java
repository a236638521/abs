package com.m7.abs.common.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.m7.abs.common.utils.FastJsonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

/**
 * 重写支持json
 * @author Wuxq
 */
@MappedTypes({Object.class})
public class JsonTypeHandler extends BaseTypeHandler{

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        if (preparedStatement != null) {
            PGobject ext = new PGobject();
            ext.setType("json");
            ext.setValue(FastJsonUtils.toJSONString(o));
            preparedStatement.setObject(i, ext);
        }
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Object object = resultSet.getObject(s);
        return object;
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
