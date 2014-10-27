package com.github.tx.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.github.tx.mybatis.annotation.AutoResultMap;
import com.github.tx.mybatis.criteria.CriteriaQuery;
import com.github.tx.mybatis.entity.Page;
import com.github.tx.mybatis.util.Constants;

/**
 * 条件查询的mapper基类
 * 
 * @author tangx
 * @since 2014年10月27日
 */

public interface CriteriaQueryMapper<T> extends CrudMapper<T> {

	/**
	 * 根据条件查找记录
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	@SelectProvider(type = SqlTemplate.class, method = "query")
	@AutoResultMap
	List<T> query(
			@Param(value = Constants.CRITERIA_KEY) CriteriaQuery query);

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
	@AutoResultMap
	List<T> queryByPage(Page page,
			@Param(value = Constants.CRITERIA_KEY) CriteriaQuery query);
}
