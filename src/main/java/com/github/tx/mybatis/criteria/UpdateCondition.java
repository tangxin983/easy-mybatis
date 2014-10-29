package com.github.tx.mybatis.criteria;

/**
 * 更新条件
 * 
 * @author tangx
 * @since 2014年10月29日
 */

public class UpdateCondition extends Condition {

	public UpdateCondition() {
		super();
	}

	@Override
	public UpdateCondition or(Criteria criteria) {
		criterias.add(criteria);
		return (UpdateCondition) this;
	}
}
