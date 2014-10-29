package com.github.tx.mybatis.test;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.entity.Page;
import com.github.tx.mybatis.test.entity.Blog;
import com.github.tx.mybatis.test.mapper.BlogMapper;

/**
 * Crud Test
 * 
 * @author tangx
 * @since 2014年10月28日
 */

public class CrudMapperTest extends AbstractMybatisTest {

//	@Test
	public void testInsert() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			for (int i = 1; i <= 10; i++) {
				Blog blog = new Blog();
				blog.setId(i);
				blog.setAuthor("author" + i);
				blog.setContent("content" + i);
				mapper.insert(blog);
			}
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testSelect() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			mapper.select();
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testSelectByPage() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Page page = new Page();
			page.setCurrentPage(1);
			page.setSize(5);
			List<Blog> list = mapper.selectByPage(page);
			Assert.assertTrue(list.size() == 5);
			Assert.assertTrue(page.getRecordsTotal() == 10);
			Assert.assertTrue(page.getPageTotal() == 2);
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testQueryByPage() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			QueryCondition query = new QueryCondition();
			query.or(Criteria.newCriteria().isNotNull("id")).desc("id")
					.setDistinct(true);
			Page page = new Page();
			page.setCurrentPage(1);
			page.setSize(5);
			List<Blog> list = mapper.queryByPage(page, query);
			Assert.assertTrue(list.size() == 5);
			Assert.assertTrue(page.getRecordsTotal() == 10);
			Assert.assertTrue(page.getPageTotal() == 2);
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testQuery() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			QueryCondition query = new QueryCondition();
			query.or(Criteria.newCriteria().eq("id", 1)).desc("id")
					.setDistinct(true);
			List<Blog> list = mapper.query(query);
			Assert.assertTrue(list.size() == 1);
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testSelectById() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = mapper.selectByPrimaryKey(1);
			Assert.assertTrue(blog.getAuthor().equals("author1"));
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testUpdate() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = new Blog();
			blog.setId(10);
			blog.setAuthor("author10-update");
			mapper.updateByPrimaryKey(blog);
		} finally {
			session.commit();
			session.close();
		}
	}


	@Test
	public void testDeleteById() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			mapper.deleteByPrimaryKey(12);
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testComplexSelect() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			List<Map> blogs = mapper.ComplexSelect();
		} finally {
			session.commit();
			session.close();
		}
	}


}
