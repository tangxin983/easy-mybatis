package com.github.tx.mybatis.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.github.tx.mybatis.criteria.Condition;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.Criteria.Criterion;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.criteria.UpdateCondition;
import com.github.tx.mybatis.util.Constants;
import com.github.tx.mybatis.util.ReflectUtil;

/**
 * sql模板
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public class SqlTemplate {

	/**
	 * 查询所有记录
	 * 
	 * @param clazz
	 * @return
	 */
	public String select(final Class<?> clazz) {
		return new SQL() {
			{
				SELECT("*");
				FROM(ReflectUtil.getTableName(clazz));
			}
		}.toString();
	}

	/**
	 * 根据主键查找记录
	 * 
	 * @param parameter
	 * @return
	 */
	public String selectByPrimaryKey(final Map<String, Object> parameter) {
		final Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
		if (StringUtils.isBlank(ReflectUtil.getIdFieldName(clazz))) {
			throw new RuntimeException(
					"selectByPrimaryKey error:cant find @Id annotation in "
							+ clazz.getName());
		}
		return new SQL() {
			{
				SELECT("*");
				FROM(ReflectUtil.getTableName(clazz));
				WHERE(ReflectUtil.getIdColumnName(clazz) + " = #{"
						+ Constants.PARA_KEY + "}");
			}
		}.toString();
	}

	/**
	 * 查询所有记录(分页)
	 * 
	 * @param parameter
	 * @return
	 */
	public String selectByPage(final Map<String, Object> parameter) {
		Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
		return select(clazz);
	}

	/**
	 * 查询记录数
	 *
	 * @param clazz
	 * @return
	 */
	public String count(final Class<?> clazz) {
		return new SQL() {
			{
				SELECT("count(1)");
				FROM(ReflectUtil.getTableName(clazz));
			}
		}.toString();
	}

	/**
	 * 根据条件查询记录数
	 * 
	 * @param parameter
	 * @return
	 */
	public String countByCondition(final Map<String, Object> parameter) {
		Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
		QueryCondition query = (QueryCondition) parameter
				.get(Constants.CRITERIA_KEY);
		SQL sql = new SQL().SELECT("count(1)");
		sql.FROM(ReflectUtil.getTableName(clazz));
		return where(sql, clazz, query);
	}

	/**
	 * 根据条件查询记录
	 * 
	 * @param parameter
	 * @return
	 */
	public String selectByCondition(final Map<String, Object> parameter) {
		Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
		QueryCondition query = (QueryCondition) parameter
				.get(Constants.CRITERIA_KEY);
		SQL sql = new SQL();
		if (query.isDistinct()) {
			sql.SELECT_DISTINCT("*");
		} else {
			sql.SELECT("*");
		}
		sql.FROM(ReflectUtil.getTableName(clazz));
		return where(sql, clazz, query);
	}

	/**
	 * 根据条件查询记录(分页)
	 * 
	 * @param parameter
	 * @return
	 */
	public String selectByConditionAndPage(final Map<String, Object> parameter) {
		return selectByCondition(parameter);
	}

	/**
	 * 插入记录
	 * 
	 * @param t
	 * @return
	 */
	public String insert(final Object t) {
		final Class<?> clazz = t.getClass();
		return new SQL() {
			{
				if (StringUtils.isNotBlank(ReflectUtil.getIdFieldName(clazz))) {
					INSERT_INTO(ReflectUtil.getTableName(clazz));
					VALUES(ReflectUtil.getIdColumnName(clazz) + ","
							+ ReflectUtil.insertColumnNameList(t), "#{"
							+ ReflectUtil.getIdFieldName(clazz) + "},"
							+ ReflectUtil.insertFieldNameList(t));
				} else {
					INSERT_INTO(ReflectUtil.getTableName(clazz));
					VALUES(ReflectUtil.insertColumnNameList(t),
							ReflectUtil.insertFieldNameList(t));
				}
			}
		}.toString();
	}

	/**
	 * 根据主键更新记录
	 * 
	 * @param t
	 * @return
	 */
	public String updateByPrimaryKey(final Object t) {
		final Class<?> clazz = t.getClass();
		if (StringUtils.isBlank(ReflectUtil.getIdFieldName(clazz))) {
			throw new RuntimeException(
					"updateByPrimaryKey error:cant find @Id annotation in "
							+ clazz.getName());
		}
		return new SQL() {
			{
				UPDATE(ReflectUtil.getTableName(clazz));
				SET(ReflectUtil.getUpdateSQL(t, false));
				WHERE(ReflectUtil.getIdColumnName(clazz) + " = #{"
						+ ReflectUtil.getIdFieldName(clazz) + "}");
			}
		}.toString();
	}

	/**
	 * 根据主键删除记录
	 * 
	 * @param parameter
	 * @return
	 */
	public String deleteByPrimaryKey(final Map<String, Object> parameter) {
		final Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
		if (StringUtils.isBlank(ReflectUtil.getIdFieldName(clazz))) {
			throw new RuntimeException(
					"deleteByPrimaryKey error:cant find @Id annotation in "
							+ clazz.getName());
		}
		return new SQL() {
			{
				DELETE_FROM(ReflectUtil.getTableName(clazz));
				WHERE(ReflectUtil.getIdColumnName(clazz) + " = #{"
						+ Constants.PARA_KEY + "}");
			}
		}.toString();
	}

	/**
	 * 根据条件更新记录
	 * 
	 * @param parameter
	 * @return
	 */
	public String updateByCondition(final Map<String, Object> parameter) {
		UpdateCondition query = (UpdateCondition) parameter
				.get(Constants.CRITERIA_KEY);
		Object t = (Object) parameter.get(Constants.ENTITY_KEY);
		Class<?> clazz = (Class<?>) t.getClass();
		SQL sql = new SQL().UPDATE(ReflectUtil.getTableName(clazz)).SET(
				ReflectUtil.getUpdateSQL(t, true));
		return where(sql, clazz, query);
	}

	/**
	 * 根据条件删除记录
	 * 
	 * @param parameter
	 * @return
	 */
	public String deleteByCondition(final Map<String, Object> parameter) {
		Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
		UpdateCondition query = (UpdateCondition) parameter
				.get(Constants.CRITERIA_KEY);
		SQL sql = new SQL().DELETE_FROM(ReflectUtil.getTableName(clazz));
		return where(sql, clazz, query);
	}

	/**
	 * 查询或更新条件
	 * 
	 * @param sql
	 * @param query
	 * @return
	 */
	private String where(SQL sql, Class<?> clazz, Condition query) {
		List<Criteria> criterias = query.getCriterias();
		int size = criterias.size();
		int i = 0, j = 0;
		for (Criteria criteria : criterias) {
			j = 0;
			for (Criterion criterion : criteria.getCriterions()) {
				String condition = criterion.getCondition();
				String property = criterion.getProperty();
				String realColumnName = getRealColumnName(clazz, property);
				condition = condition.replace(property, realColumnName);// 替换condition中的属性名为真实列名
				Object value = criterion.getValue();
				Object secondValue = criterion.getSecondValue();
				StringBuffer sb = new StringBuffer();
				if (criterion.isNoValue()) {
					sql.WHERE(condition);
				} else if (criterion.isSingleValue() && value != null) {
					sb.append("#{");
					sb.append(Constants.CRITERIA_KEY + ".");
					sb.append("criterias[" + i + "].");
					sb.append("criterions[" + j + "].value");
					sb.append("}");
					sql.WHERE(condition + sb.toString());
				} else if (criterion.isBetweenValue() && value != null
						&& secondValue != null) {
					sb.append("#{");
					sb.append(Constants.CRITERIA_KEY + ".");
					sb.append("criterias[" + i + "].");
					sb.append("criterions[" + j + "].value");
					sb.append("}");
					sb.append(" and #{");
					sb.append(Constants.CRITERIA_KEY + ".");
					sb.append("criterias[" + i + "].");
					sb.append("criterions[" + j + "].secondValue");
					sb.append("}");
					sql.WHERE(condition + sb.toString());
				} else if (criterion.isListValue() && value != null) {
					List<?> valueList = (List<?>) criterion.getValue();
					if (valueList.size() > 0) {
						sb.append("(");
						for (int x = 0; x < valueList.size(); x++) {
							sb.append("#{");
							sb.append(Constants.CRITERIA_KEY + ".");
							sb.append("criterias[" + i + "].");
							sb.append("criterions[" + j + "].");
							sb.append("value[" + x + "]");
							sb.append("}");
							if (x != valueList.size() - 1) {
								sb.append(",");
							}
						}
						sb.append(")");
					}
					sql.WHERE(condition + sb.toString());
				}
				j++;
			}
			if (--size > 0) {
				sql.OR();
			}
			i++;
		}
		if (query instanceof QueryCondition) {
			QueryCondition queryCondition = (QueryCondition) query;
			Set<String> groupBys = queryCondition.getGroupByColumns();
			for (String column : groupBys) {
				if (StringUtils.isNotBlank(column)) {
					sql.GROUP_BY(getRealColumnName(clazz, column));
				}
			}
			Set<String> ascs = queryCondition.getAscColumns();
			for (String column : ascs) {
				if (StringUtils.isNotBlank(column)) {
					sql.ORDER_BY(getRealColumnName(clazz, column) + " asc");
				}
			}
			Set<String> descs = queryCondition.getDescColumns();
			for (String column : descs) {
				if (StringUtils.isNotBlank(column)) {
					sql.ORDER_BY(getRealColumnName(clazz, column) + " desc");
				}
			}
		}
		return sql.toString();
	}
	
	/**
	 * 通过字段名找列名
	 * @param clazz
	 * @param property
	 * @return
	 */
	private String getRealColumnName(Class<?> clazz, String property){
		// 如果property是主键
		if (property.equals(ReflectUtil.getIdColumnName(clazz))
				|| property.equals(ReflectUtil.getIdFieldName(clazz))) {
			return ReflectUtil.getIdColumnName(clazz);
		}else{// 如果property不是主键
			String columnName = ReflectUtil.getColumnNameByFieldName(clazz, property);
			if (StringUtils.isNotBlank(columnName)) {
				return columnName;
			}
		}
		return property;
	}
}
