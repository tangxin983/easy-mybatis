package com.github.tx.mybatis.criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建用and连接的条件
 * @author tangx
 * @since 2014年10月27日
 */

public class Criteria {
	
	protected List<Criterion> criterions;
	
	public Criteria() {
		criterions = new ArrayList<Criterion>();
	}
	
	public static Criteria newCriteria(){
		return new Criteria();
	}
 
	public List<Criterion> getCriterions() {
		return criterions;
	}

	protected void addCriterion(String condition) {
		if (condition == null) {
			throw new RuntimeException("Value for condition cannot be null");
		}
		criterions.add(new Criterion(condition));
	}

	protected void addCriterion(String condition, Object value) {
		if (value == null) {
			throw new RuntimeException("value cannot be null");
		}
		criterions.add(new Criterion(condition, value));
	}

	protected void addCriterion(String condition, Object value1, Object value2) {
		if (value1 == null || value2 == null) {
			throw new RuntimeException("value cannot be null");
		}
		criterions.add(new Criterion(condition, value1, value2));
	}

	public Criteria isNull(String property) {
		addCriterion(property + " is null");
		return (Criteria) this;
	}
	
	public Criteria isNotNull(String property) {
		addCriterion(property + " is not null");
		return (Criteria) this;
	}

	public Criteria eq(String property, Object value) {
		addCriterion(property + " = ", value);
		return (Criteria) this;
	}

	public Criteria notEq(String property, Object value) {
		addCriterion(property + " <> ", value);
		return (Criteria) this;
	}

	public Criteria gt(String property, Object value) {
		addCriterion(property + " > ", value);
		return (Criteria) this;
	}

	public Criteria ge(String property, Object value) {
		addCriterion(property + " >= ", value);
		return (Criteria) this;
	}

	public Criteria lt(String property, Object value) {
		addCriterion(property + " < ", value);
		return (Criteria) this;
	}

	public Criteria le(String property, Object value) {
		addCriterion(property + " <= ", value);
		return (Criteria) this;
	}

	public Criteria like(String property, Object value) {
		addCriterion(property + " like ", value);
		return (Criteria) this;
	}

	public Criteria notLike(String property, Object value) {
		addCriterion(property + " not like ", value);
		return (Criteria) this;
	}

	public Criteria in(String property, List<?> values) {
		addCriterion(property + " in ", values);
		return (Criteria) this;
	}

	public Criteria notIn(String property, List<?> values) {
		addCriterion(property + " not in ", values);
		return (Criteria) this;
	}

	public Criteria between(String property, Object value1, Object value2) {
		addCriterion(property + " between ", value1, value2);
		return (Criteria) this;
	}

	public Criteria notBetween(String property, Object value1, Object value2) {
		addCriterion(property + " not between ", value1, value2);
		return (Criteria) this;
	}

	public static class Criterion {
		private String condition;

		private Object value;

		private Object secondValue;

		private boolean noValue;

		private boolean singleValue;

		private boolean betweenValue;

		private boolean listValue;

		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue,
				String typeHandler) {
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}
}
