package com.github.tx.mybatis.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.mapper.CrudMapper;
import com.github.tx.mybatis.test.entity.Blog;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public interface BlogMapper extends CrudMapper<Blog> {
  
	public List<Blog> selectByXml(@Param("condition") QueryCondition condition);
}
