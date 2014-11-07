package com.github.tx.mybatis.criteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.tx.mybatis.criteria.Criteria.Criterion;
import com.github.tx.mybatis.util.ReflectUtil;

/**
 * sql条件基类
 * 
 * @author tangx
 * @since 2014年10月27日
 */

public abstract class Condition {

	protected List<Criteria> criterias;

	public Condition() {
		criterias = new ArrayList<Criteria>();
	}

	public List<Criteria> getCriterias() {
		return criterias;
	}

	public abstract Condition or(Criteria criteria);

	public void clear() {
		criterias.clear();
	}

	/**
	 * 将条件中的字段名转换为真实列名,如果已经是真实列名则不变
	 * 
	 * @param clazz
	 * @param property
	 * @return
	 */
	public void transform(Class<?> clazz) {
		List<Criteria> criterias = this.getCriterias();
		for (Criteria criteria : criterias) {
			for (Criterion criterion : criteria.getCriterions()) {
				String condition = criterion.getCondition();
				String property = criterion.getProperty();
				String realColumnName = getRealColumnName(clazz, property);
				criterion.setCondition(condition.replace(property,
						realColumnName));// 替换condition中的属性名为真实列名
			}
		}
	}

	/**
	 * 通过字段名找列名
	 * 
	 * @param clazz
	 * @param property
	 * @return
	 */
	protected String getRealColumnName(Class<?> clazz, String property) {
		// 如果property是主键
		if (property.equals(ReflectUtil.getIdColumnName(clazz))
				|| property.equals(ReflectUtil.getIdFieldName(clazz))) {
			return ReflectUtil.getIdColumnName(clazz);
		} else {// 如果property不是主键
			String columnName = ReflectUtil.getColumnNameByFieldName(clazz,
					property);
			if (StringUtils.isNotBlank(columnName)) {
				return columnName;
			}
		}
		return property;
	}
}
