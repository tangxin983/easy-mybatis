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
	 * @return 实体列表
	 */
	@SelectProvider(type = SqlTemplate.class, method = "select")
	@AutoMapping
	List<T> select();

	/**
	 * 查找所有记录（分页）
	 * 
	 * @param page
	 * @return 实体列表
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectByPage")
	@AutoMapping
	List<T> selectByPage(Page page);

	/**
	 * 根据主键查找记录
	 * 
	 * @param id
	 * @return 实体对象
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectByPrimaryKey")
	@AutoMapping
	T selectByPrimaryKey(Serializable id);

	/**
	 * 根据条件查找记录
	 * 
	 * @param query
	 *            查询条件
	 * @return 实体列表
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectByCondition")
	@AutoMapping
	List<T> selectByCondition(
			@Param(value = Constants.CRITERIA_KEY) QueryCondition condition);

	/**
	 * 根据条件查找记录（分页）
	 * 
	 * @param page
	 *            分页信息
	 * @param query
	 *            查询条件
	 * @return 实体列表
	 */
	@SelectProvider(type = SqlTemplate.class, method = "selectByConditionAndPage")
	@AutoMapping
	List<T> selectByConditionAndPage(
			@Param(value = Constants.CRITERIA_KEY) QueryCondition condition,
			Page page);

	/**
	 * 查询记录数
	 * 
	 * @return 记录数
	 */
	@SelectProvider(type = SqlTemplate.class, method = "count")
	@AutoMapping
	int count();
	
	/**
	 * 根据条件查询记录数
	 * 
	 * @return 记录数
	 */
	@SelectProvider(type = SqlTemplate.class, method = "countByCondition")
	@AutoMapping
	int countByCondition(@Param(value = Constants.CRITERIA_KEY) QueryCondition condition);

	/**
	 * 插入记录
	 * 
	 * @param t
	 *            实体对象
	 * @return 插入影响的记录数
	 */
	@InsertProvider(type = SqlTemplate.class, method = "insert")
	int insert(T t);

	/**
	 * 根据主键更新记录
	 * 
	 * @param t
	 *            实体对象
	 * @return 更新影响的记录数
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateByPrimaryKey")
	int updateByPrimaryKey(T t);

	/**
	 * 根据条件更新记录
	 * 
	 * @param t
	 *            实体对象
	 * @param condition
	 *            更新条件
	 * @return 更新影响的记录数
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "updateByCondition")
	int updateByCondition(@Param(value = Constants.ENTITY_KEY) T t,
			@Param(value = Constants.CRITERIA_KEY) UpdateCondition condition);

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 *            主键值
	 * @return 删除影响的记录数
	 */
	@DeleteProvider(type = SqlTemplate.class, method = "deleteByPrimaryKey")
	@AutoMapping
	int deleteByPrimaryKey(Serializable id);

	/**
	 * 根据条件删除记录
	 * 
	 * @param condition
	 *            删除条件
	 * @return 删除影响的记录数
	 */
	@UpdateProvider(type = SqlTemplate.class, method = "deleteByCondition")
	@AutoMapping
	int deleteByCondition(
			@Param(value = Constants.CRITERIA_KEY) UpdateCondition condition);
}
