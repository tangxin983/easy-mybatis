package com.github.tx.mybatis.criteria;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 查询条件
 * 
 * @author tangx
 * @since 2014年10月29日
 */

public class QueryCondition extends Condition {

	protected Set<String> descColumns;

	protected Set<String> ascColumns;

	protected Set<String> groupByColumns;

	protected boolean distinct;

	public QueryCondition() {
		super();
		descColumns = new HashSet<String>();
		ascColumns = new HashSet<String>();
		groupByColumns = new HashSet<String>();
	}

	@Override
	public QueryCondition or(Criteria criteria) {
		criterias.add(criteria);
		return (QueryCondition) this;
	}

	public QueryCondition desc(String column) {
		descColumns.add(column);
		return (QueryCondition) this;
	}

	public QueryCondition asc(String column) {
		ascColumns.add(column);
		return (QueryCondition) this;
	}

	public QueryCondition groupBy(String column) {
		groupByColumns.add(column);
		return (QueryCondition) this;
	}

	public QueryCondition desc(String[] columns) {
		descColumns.addAll(Arrays.asList(columns));
		return (QueryCondition) this;
	}

	public QueryCondition asc(String[] columns) {
		ascColumns.addAll(Arrays.asList(columns));
		return (QueryCondition) this;
	}

	public QueryCondition groupBy(String[] columns) {
		groupByColumns.addAll(Arrays.asList(columns));
		return (QueryCondition) this;
	}

	public Set<String> getDescColumns() {
		return descColumns;
	}

	public Set<String> getAscColumns() {
		return ascColumns;
	}

	public Set<String> getGroupByColumns() {
		return groupByColumns;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public QueryCondition setDistinct(boolean distinct) {
		this.distinct = distinct;
		return (QueryCondition) this;
	}

	@Override
	public void clear() {
		super.clear();
		descColumns.clear();
		ascColumns.clear();
		groupByColumns.clear();
		distinct = false;
	}

	@Override
	public QueryCondition transform(Class<?> clazz) {
		super.transform(clazz);
		groupByColumns = transformSet(clazz, groupByColumns);
		descColumns = transformSet(clazz, descColumns);
		ascColumns = transformSet(clazz, ascColumns);
		return this;
	}
	
	private Set<String> transformSet(Class<?> clazz, Set<String> oldSet){
		Set<String> newSet = new HashSet<String>();
		for (String column : oldSet) {
			newSet.add(getRealColumnName(clazz, column));
		}
		return newSet;
	}
}
