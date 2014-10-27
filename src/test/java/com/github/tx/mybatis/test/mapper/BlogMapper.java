package com.github.tx.mybatis.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.github.tx.mybatis.entity.Page;
import com.github.tx.mybatis.mapper.CriteriaQueryMapper;
import com.github.tx.mybatis.test.entity.Blog;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public interface BlogMapper extends CriteriaQueryMapper<Blog> {
  
	@Select("select * from blog where id=#{param1}")
	public List<Blog> selectByPageAndId(int id, Page page);
}
