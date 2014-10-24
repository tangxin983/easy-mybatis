package com.github.tx.mybatis.mapper;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.github.tx.mybatis.util.ReflectUtil;

/**
 * Crud模板
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public class CrudTemplate {
	
	public static final String CLASS_KEY = "clazz";

	public static final String PARA_KEY = "para";
	
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
	 * 根据主键查找记录
	 * @param parameter
	 * @return
	 */
	public String selectById(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<?> clazz = (Class<?>) parameter.get(CLASS_KEY);
				SELECT("*");
				FROM(ReflectUtil.getTableName(clazz));
				WHERE(ReflectUtil.getIdColumnName(clazz) + " = #{" + PARA_KEY + "}");
			}
		}.toString();
	}
	
	/**
	 * 查询所有记录(分页)
	 * @param parameter
	 * @return
	 */
	public String selectByPage(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<?> clazz = (Class<?>) parameter.get(CLASS_KEY);
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
	
	/**
	 * 根据主键删除记录
	 * @param t
	 * @return
	 */
	public String deleteById(final Map<String, Object> parameter) {
		return new SQL() {
			{
				Class<?> clazz = (Class<?>) parameter.get(CLASS_KEY);
				DELETE_FROM(ReflectUtil.getTableName(clazz));
				WHERE(ReflectUtil.getIdColumnName(clazz) + " = #{" + PARA_KEY + "}");
			}
		}.toString();
	}
}
