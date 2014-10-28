package com.github.tx.mybatis.test.mapper;

import java.util.List;
import java.util.Map;

import com.github.tx.mybatis.mapper.CrudMapper;
import com.github.tx.mybatis.test.entity.Blog;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public interface BlogMapper extends CrudMapper<Blog> {
  
	public List<Map> ComplexSelect();
}
