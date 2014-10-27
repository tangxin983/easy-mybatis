package com.github.tx.mybatis.mapper;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.github.tx.mybatis.annotation.AutoResultMap;
import com.github.tx.mybatis.entity.Page;

/**
 * 增删改查Mapper基类
 * @author tangx
 * @since 2014年10月22日
 */

public interface CrudMapper<T> {
	
	/**
	 * 查找所有记录
	 * @return 
	 */
	@SelectProvider(type = SqlTemplate.class, method = "select")
	@AutoResultMap
	List<T> select();
	
	/**
	 * 查找所有记录（分页）
	 * @param page
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectByPage")
	@AutoResultMap
	List<T> selectByPage(Page page);
	
	/**
	 * 根据主键查找记录
	 * @param id
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectById")
	@AutoResultMap
	T selectById(Serializable id);

	/**
	 * 插入记录
	 * @param t
	 */
	@InsertProvider(type = SqlTemplate.class, method = "insert")
	void insert(T t);

	/**
	 * 更新记录
	 * @param t
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "update")
	void update(T t);

	/**
	 * 删除记录
	 * @param t
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "delete")
	void delete(T t);
	
	/**
	 * 根据主键删除记录
	 * @param id
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteById")
	@AutoResultMap
	void deleteById(Serializable id);
}
