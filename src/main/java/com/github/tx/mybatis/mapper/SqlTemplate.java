package com.github.tx.mybatis.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.Criteria.Criterion;
import com.github.tx.mybatis.criteria.CriteriaQuery;
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
	public String selectById(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
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
	 * 根据条件查询记录
	 * 
	 * @param parameter
	 * @return
	 */
	public String query(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
				CriteriaQuery query = (CriteriaQuery) parameter
						.get(Constants.CRITERIA_KEY);
				if (query.isDistinct()) {
					SELECT_DISTINCT("*");
				} else {
					SELECT("*");
				}
				FROM(ReflectUtil.getTableName(clazz));
				List<Criteria> criterias = query.getCriterias();
				int size = criterias.size();
				int i = 0, j = 0;
				for (Criteria criteria : criterias) {
					j = 0;
					for (Criterion criterion : criteria.getCriterions()) {
						String condition = criterion.getCondition();
						Object value = criterion.getValue();
						Object secondValue = criterion.getSecondValue();
						StringBuffer sb = new StringBuffer();
						if (criterion.isNoValue()) {
							WHERE(condition);
						} else if (criterion.isSingleValue() && value != null) {
							sb.append("#{");
							sb.append(Constants.CRITERIA_KEY + ".");
							sb.append("criterias[" + i + "].");
							sb.append("criterions[" + j + "].value");
							sb.append("}");
							WHERE(condition + sb.toString());
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
							WHERE(condition + sb.toString());
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
							WHERE(condition + sb.toString());
						}
						j++;
					}
					if (--size > 0) {
						OR();
					}
					i++;
				}
				Set<String> groupBys = query.getGroupByColumns();
				for (String column : groupBys) {
					if (StringUtils.isNotBlank(column)) {
						GROUP_BY(column);
					}
				}
				Set<String> ascs = query.getAscColumns();
				for (String column : ascs) {
					if (StringUtils.isNotBlank(column)) {
						ORDER_BY(column + " asc");
					}
				}
				Set<String> descs = query.getDescColumns();
				for (String column : descs) {
					if (StringUtils.isNotBlank(column)) {
						ORDER_BY(column + " desc");
					}
				}
			}
		}.toString();
	}

	/**
	 * 插入
	 * 
	 * @param t
	 * @return
	 */
	public String insert(final Object t) {
		return new SQL() {
			{
				INSERT_INTO(ReflectUtil.getTableName(t.getClass()));
				VALUES(ReflectUtil.getIdColumnName(t.getClass()) + ","
						+ ReflectUtil.insertColumnNameList(t), "#{"
						+ ReflectUtil.getIdFieldName(t.getClass()) + "},"
						+ ReflectUtil.insertFieldNameList(t));
			}
		}.toString();
	}

	/**
	 * 更新
	 * 
	 * @param t
	 * @return
	 */
	public String update(final Object t) {
		return new SQL() {
			{
				UPDATE(ReflectUtil.getTableName(t.getClass()));
				SET(ReflectUtil.getUpdateSQL(t));
				WHERE(ReflectUtil.getIdColumnName(t.getClass()) + " = #{"
						+ ReflectUtil.getIdFieldName(t.getClass()) + "}");
			}
		}.toString();
	}

	/**
	 * 删除
	 * 
	 * @param t
	 * @return
	 */
	public String delete(final Object t) {
		return new SQL() {
			{
				DELETE_FROM(ReflectUtil.getTableName(t.getClass()));
				WHERE(ReflectUtil.getIdColumnName(t.getClass()) + " = #{"
						+ ReflectUtil.getIdFieldName(t.getClass()) + "}");
			}
		}.toString();
	}

	/**
	 * 根据主键删除记录
	 * 
	 * @param t
	 * @return
	 */
	public String deleteById(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<?> clazz = (Class<?>) parameter.get(Constants.CLASS_KEY);
				DELETE_FROM(ReflectUtil.getTableName(clazz));
				WHERE(ReflectUtil.getIdColumnName(clazz) + " = #{"
						+ Constants.PARA_KEY + "}");
			}
		}.toString();
	}
}
