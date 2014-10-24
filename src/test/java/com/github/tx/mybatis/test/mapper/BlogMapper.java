package com.github.tx.mybatis.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.github.tx.mybatis.core.Page;
import com.github.tx.mybatis.mapper.BaseMapper;
import com.github.tx.mybatis.test.entity.Blog;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public interface BlogMapper extends BaseMapper<Blog> {

	@Select("SELECT * FROM blog")
	List<Blog> selectBlog(Page page);
	
	@Select("SELECT * FROM blog")
	List<Blog> selectAll();

	@Select("SELECT * FROM blog WHERE id = #{param1}")
	Blog selectByIdAndPage(int id, Page page);
	
	@Select("SELECT * FROM blog WHERE id = #{id}")
	Blog selectById(int id);
}
