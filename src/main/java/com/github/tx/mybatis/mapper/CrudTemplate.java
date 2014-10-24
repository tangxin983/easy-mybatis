package com.github.tx.mybatis.mapper;

import org.apache.ibatis.jdbc.SQL;

import com.github.tx.mybatis.util.ReflectUtil;

/**
 * Crud模板
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public class CrudTemplate {
	
	/**
	 * 查询所有记录
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
}
