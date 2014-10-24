package com.github.tx.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Mapper基类
 * @author tangx
 * @since 2014年10月22日
 */

public interface BaseMapper<T> {
	
	@SelectProvider(type = CrudTemplate.class, method = "select")
	public List<T> select();

	@InsertProvider(type = CrudTemplate.class, method = "insert")
	public void insert(T t);

	@UpdateProvider(type = CrudTemplate.class, method = "update")
	public void update(T t);

	@DeleteProvider(type = CrudTemplate.class, method = "delete")
	public void delete(T t);
}
