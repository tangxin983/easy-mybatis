package com.github.tx.mybatis.mapper;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.github.tx.mybatis.annotation.AutoMapping;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.criteria.UpdateCondition;
import com.github.tx.mybatis.entity.Page;
import com.github.tx.mybatis.util.Constants;

/**
 * 增删改查Mapper基类
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public interface CrudMapper<T> {

	/**
	 * 查找所有记录
	 * 
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "select")
	@AutoMapping
	List<T> select();

	/**
	 * 查找所有记录（分页）
	 * 
	 * @param page
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectByPage")
	@AutoMapping
	List<T> selectByPage(Page page);

	/**
	 * 根据主键查找记录
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectByPrimaryKey")
	@AutoMapping
	T selectByPrimaryKey(Serializable id);

	/**
	 * 查询记录总数
	 * 
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "count")
	@AutoMapping
	int count();

	/**
	 * 根据条件查找记录
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "query")
	@AutoMapping
	List<T> query(
			@Param(value = Constants.CRITERIA_KEY) QueryCondition condition);

	/**
	 * 根据条件查找记录（分页）
	 * 
	 * @param page
	 *            分页信息
	 * @param query
	 *            查询条件
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "query")
	@AutoMapping
	List<T> queryByPage(Page page,
			@Param(value = Constants.CRITERIA_KEY) QueryCondition condition);

	/**
	 * 插入记录
	 * 
	 * @param t
	 */
	@InsertProvider(type = SqlTemplate.class, method = "insert")
	void insert(T t);

	/**
	 * 根据主键更新记录
	 * 
	 * @param t
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateByPrimaryKey")
	void updateByPrimaryKey(T t);

	/**
	 * 根据条件更新记录
	 * 
	 * @param t
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateByCondition")
	void updateByCondition(@Param(value = Constants.ENTITY_KEY) T t,
			@Param(value = Constants.CRITERIA_KEY) UpdateCondition condition);

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteByPrimaryKey")
	@AutoMapping
	void deleteByPrimaryKey(Serializable id);

	/**
	 * 根据条件删除记录
	 * 
	 * @param t
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "deleteByCondition")
	@AutoMapping
	void deleteByCondition(@Param(value = Constants.CRITERIA_KEY) UpdateCondition condition);
}
