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
 * Mapper基类
 * @author tangx
 * @since 2014年10月22日
 */

public interface BasicCrud<T> {
	
	/**
	 * 查找所有记录
	 * @return 
	 */
	@SelectProvider(type = BasicCrudTemplate.class, method = "select")
	@AutoResultMap
	public List<T> select();
	
	/**
	 * 查找所有记录（分页）
	 * @param page
	 * @return
	 */
	@SelectProvider(type = BasicCrudTemplate.class, method = "selectByPage")
	@AutoResultMap
	List<T> selectByPage(Page page);
	
	/**
	 * 根据主键查找记录
	 * @param id
	 * @return
	 */
	@SelectProvider(type = BasicCrudTemplate.class, method = "selectById")
	@AutoResultMap
	public T selectById(Serializable id);

	/**
	 * 插入记录
	 * @param t
	 */
	@InsertProvider(type = BasicCrudTemplate.class, method = "insert")
	public void insert(T t);

	/**
	 * 更新记录
	 * @param t
	 */
	@UpdateProvider(type = BasicCrudTemplate.class, method = "update")
	public void update(T t);

	/**
	 * 删除记录
	 * @param t
	 */
	@DeleteProvider(type = BasicCrudTemplate.class, method = "delete")
	public void delete(T t);
	
	/**
	 * 根据主键删除记录
	 * @param id
	 */
	@DeleteProvider(type = BasicCrudTemplate.class, method = "deleteById")
	@AutoResultMap
	public void deleteById(Serializable id);
}
