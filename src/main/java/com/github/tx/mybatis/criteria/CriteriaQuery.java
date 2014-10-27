package com.github.tx.mybatis.criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author tangx
 * @since 2014年10月27日
 */

public class CriteriaQuery {

	protected List<Criteria> oredCriteria;

	public CriteriaQuery() {
		oredCriteria = new ArrayList<Criteria>();
	}

	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	public CriteriaQuery or(Criteria criteria) {
		oredCriteria.add(criteria);
		return (CriteriaQuery) this;
	}

	public CriteriaQuery where(Criteria criteria) {
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return (CriteriaQuery) this;
	}

	public void clear() {
		oredCriteria.clear();
		// orderByClause = null;
		// distinct = false;
	}
}
