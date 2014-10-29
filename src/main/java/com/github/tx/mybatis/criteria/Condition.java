package com.github.tx.mybatis.criteria;

import java.util.ArrayList;
import java.util.List;

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
}
