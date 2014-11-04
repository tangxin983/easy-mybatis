package com.github.tx.mybatis.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;

import com.github.tx.mybatis.entity.Page;

/**
 * Mybatis数据库物理分页插件
 * 
 * @author tangx
 * @since 2014年10月23日
 */

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor extends BaseInterceptor implements Interceptor {

	private String dialect;

	private static final String DEFAULT_DIALECT = "mysql";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		MetaObject metaStatementHandler = getMetaObject(statementHandler);
		// 如果传入的参数中有分页对象且sql语句中有select，才做分页处理
		BoundSql boundSql = statementHandler.getBoundSql();
		Object paramObj = boundSql.getParameterObject();
		String sql = boundSql.getSql().toLowerCase();
		Page page = retrievePageFromParam(paramObj);
		if (sql.indexOf("select") != -1 && page != null) {
			// 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
			metaStatementHandler.setValue("delegate.rowBounds.offset",
					RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit",
					RowBounds.NO_ROW_LIMIT);
			// 设置分页对象里的总记录数和总页数
			Connection connection = (Connection) invocation.getArgs()[0];
			MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
					.getValue("delegate.mappedStatement");
			int recordsTotal = getTotalCount(sql, connection, mappedStatement,
					boundSql);
			logger.debug("recordsTotal=" + recordsTotal);
			page.setRecordsTotal(recordsTotal);
			int pageTotal = recordsTotal / page.getSize()
					+ ((recordsTotal % page.getSize() == 0) ? 0 : 1);
			logger.debug("pageTotal=" + pageTotal);
			page.setPageTotal(pageTotal);
			// 最后重写sql
			String pageSql = buildPageSql(sql, page);
			metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
		}
		return invocation.proceed();
	}

	/**
	 * 获取总记录数
	 * 
	 * @param sql
	 *            原始sql语句
	 * @param conn
	 * @param ms
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 */
	private int getTotalCount(String sql, Connection conn, MappedStatement ms,
			BoundSql boundSql) throws SQLException {
		int index = sql.indexOf("from");
		if (index == -1) {
			throw new RuntimeException("statement has no 'from' keyword");
		}
		String countSql = "select count(0) " + sql.substring(index);
		BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql,
				boundSql.getParameterMappings(), boundSql.getParameterObject());
		ParameterHandler parameterHandler = new DefaultParameterHandler(ms,
				boundSql.getParameterObject(), countBoundSql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(countSql);
			// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			rs.close();
			pstmt.close();
		}
		return count;
	}

	/**
	 * 生成特定数据库的分页语句
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String buildPageSql(String sql, Page page) {
		if (page == null || dialect == null || dialect.equals("")) {
			return sql;
		}
		StringBuilder sb = new StringBuilder();
		int startRow = 0;
		if (page.getStart() != 0) {
			startRow = page.getStart();
		} else {
			startRow = (page.getCurrentPage() - 1) * page.getSize();
		}
		if (startRow <= 0) {
			startRow = 0;
		}
		if ("hsqldb".equals(dialect)) {
			String s = sql;
			sb.append("select limit ");
			sb.append(startRow);
			sb.append(" ");
			sb.append(page.getSize());
			sb.append(" ");
			sb.append(s.substring(6));
		} else if ("mysql".equals(dialect)) {
			sb.append(sql);
			sb.append(" limit " + startRow + "," + page.getSize());
		} else if ("oracle".equals(dialect)) {
			sb.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
			sb.append(sql);
			sb.append(")  tmp_tb where ROWNUM<=");
			sb.append(startRow + page.getSize());
			sb.append(") where row_id>");
			sb.append(startRow);
		} else {
			throw new IllegalArgumentException(
					"SelectInterceptor error:does not support " + dialect);
		}
		return sb.toString();
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		String dialect = properties.getProperty("dialect");
		if (StringUtils.isBlank(dialect)) {
			dialect = DEFAULT_DIALECT;
		}
		setDialect(dialect);
	}

}
