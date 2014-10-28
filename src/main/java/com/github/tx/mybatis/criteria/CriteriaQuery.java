package com.github.tx.mybatis.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author tangx
 * @since 2014年10月27日
 */

public class CriteriaQuery {

	protected List<Criteria> criterias;

	protected Set<String> descColumns;

	protected Set<String> ascColumns;

	protected Set<String> groupByColumns;

	protected boolean distinct;

	public CriteriaQuery() {
		criterias = new ArrayList<Criteria>();
		descColumns = new HashSet<String>();
		ascColumns = new HashSet<String>();
		groupByColumns = new HashSet<String>();
	}

	public List<Criteria> getCriterias() {
		return criterias;
	}

	public CriteriaQuery or(Criteria criteria) {
		criterias.add(criteria);
		return (CriteriaQuery) this;
	}

	public CriteriaQuery where(Criteria criteria) {
		if (criterias.size() == 0) {
			criterias.add(criteria);
		}
		return (CriteriaQuery) this;
	}

	public CriteriaQuery desc(String column) {
		descColumns.add(column);
		return (CriteriaQuery) this;
	}

	public CriteriaQuery asc(String column) {
		ascColumns.add(column);
		return (CriteriaQuery) this;
	}

	public CriteriaQuery groupBy(String column) {
		groupByColumns.add(column);
		return (CriteriaQuery) this;
	}

	public CriteriaQuery desc(String[] columns) {
		descColumns.addAll(Arrays.asList(columns));
		return (CriteriaQuery) this;
	}

	public CriteriaQuery asc(String[] columns) {
		ascColumns.addAll(Arrays.asList(columns));
		return (CriteriaQuery) this;
	}

	public CriteriaQuery groupBy(String[] columns) {
		groupByColumns.addAll(Arrays.asList(columns));
		return (CriteriaQuery) this;
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

	public CriteriaQuery setDistinct(boolean distinct) {
		this.distinct = distinct;
		return (CriteriaQuery) this;
	}

	public void clear() {
		criterias.clear();
		descColumns.clear();
		ascColumns.clear();
		distinct = false;
	}
}
