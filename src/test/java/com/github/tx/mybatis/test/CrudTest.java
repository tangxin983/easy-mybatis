package com.github.tx.mybatis.test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.github.tx.mybatis.entity.Page;
import com.github.tx.mybatis.test.entity.Blog;
import com.github.tx.mybatis.test.mapper.BlogMapper;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public class CrudTest extends AbstractMybatisTest {

	@Test
	public void insert() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
//			List<Blog> blogs = mapper.select(Blog.class);
			List<Blog> blogs = mapper.select();
			logger.debug(blogs.size() + "");
//			Page page = new Page();
//			page.setSize(2);
//			Blog blog = mapper.selectById(1);
//			Blog blog = mapper.selectByIdAndPage(1,page);
//			logger.debug(blog.getAuthor());
		} finally {
			session.close();
		}
	}
	 
}
