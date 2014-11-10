package com.github.tx.mybatis.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.QueryCondition;
import com.github.tx.mybatis.criteria.UpdateCondition;
import com.github.tx.mybatis.test.entity.Blog;
import com.github.tx.mybatis.test.mapper.BlogMapper;

/**
 * CrudMapperTest
 * 
 * @author tangx
 * @since 2014年10月28日
 */

public class CrudMapperTest extends AbstractMybatisTest {
	
	@Test
	public void testSelect() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			List<Blog> blogs = mapper.select();
			assertEquals(20, blogs.size());
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
			PageHelper.startPage(1, 10);
			List<Blog> blogs = mapper.select();
			PageHelper.startPage(1, 10);
			List<Blog> new_blogs = mapper.select();
			assertEquals(10, new_blogs.size());
			assertEquals(20, ((Page) new_blogs).getTotal());
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testSelectByConditionAndPage() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			QueryCondition condition = new QueryCondition();
			condition.or(Criteria.newCriteria().isNotNull("id"));
			PageHelper.startPage(1, 10);
			List<Blog> blogs = mapper.selectByCondition(condition);
			PageHelper.startPage(1, 10);
			List<Blog> new_blogs = mapper.selectByCondition(condition);
			assertEquals(10, new_blogs.size());
			assertEquals(20, ((Page) new_blogs).getTotal());
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testSelectByPrimaryKey() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = mapper.selectByPrimaryKey(1);
			assertEquals("author1", blog.getAuthor());
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testSelectByCondition() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			QueryCondition condition = new QueryCondition();
			condition.or(Criteria.newCriteria().eq("id", 1)).desc("id").setDistinct(true);
			List<Blog> blogs = mapper.selectByCondition(condition);
			assertEquals(1, blogs.size());
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testSelectByXml() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			QueryCondition condition = new QueryCondition();
			condition.or(Criteria.newCriteria().eq("id", 1)).desc("id").setDistinct(true);
			List<Blog> blogs = mapper.selectByXml(condition);
			assertEquals(1, blogs.size());
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testCount() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			assertEquals(20, mapper.count());
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testCountByCondition() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			QueryCondition condition = new QueryCondition();
			condition.or(Criteria.newCriteria().gt("id", 10));
			assertEquals(10, mapper.countByCondition(condition));
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testUpdateByPrimaryKey() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = new Blog();
			blog.setId(10);
			blog.setAuthor("author-update");
			mapper.updateByPrimaryKey(blog);
			assertEquals("author-update", mapper.selectByPrimaryKey(10).getAuthor());
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testUpdateByCondition() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = new Blog();
			blog.setAuthor("author-update");
			UpdateCondition update = new UpdateCondition();
			update.or(Criteria.newCriteria().between("id", 1, 10));
			mapper.updateByCondition(blog, update);
			QueryCondition query = new QueryCondition();
			query.or(Criteria.newCriteria().eq("author", "author-update"));
			List<Blog> blogs = mapper.selectByCondition(query);
			assertEquals(10, blogs.size());
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testDeleteByPrimaryKey() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			mapper.deleteByPrimaryKey(10);
			assertEquals(null, mapper.selectByPrimaryKey(10));
		} finally {
			session.commit();
			session.close();
		}
	}
	
	@Test
	public void testDeleteByCondition() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			UpdateCondition update = new UpdateCondition();
			update.or(Criteria.newCriteria().between("id", 1, 10));
			mapper.deleteByCondition(update);
			assertEquals(10, mapper.count());
		} finally {
			session.commit();
			session.close();
		}
	}

	@Test
	public void testInsert() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = new Blog();
			blog.setId(21);
			blog.setAuthor("author" + 21);
			blog.setCreateTime(new Date());
			mapper.insert(blog);
			assertEquals(21, mapper.count());
		} finally {
			session.commit();
			session.close();
		}
	}
}
