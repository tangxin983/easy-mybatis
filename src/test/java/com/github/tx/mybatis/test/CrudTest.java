package com.github.tx.mybatis.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.github.tx.mybatis.criteria.Criteria;
import com.github.tx.mybatis.criteria.CriteriaQuery;
import com.github.tx.mybatis.entity.Page;
import com.github.tx.mybatis.test.entity.Blog;
import com.github.tx.mybatis.test.mapper.BlogMapper;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public class CrudTest extends AbstractMybatisTest {

	// @Test
	public void insert() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Random random = new Random();
			int randomId = Math.abs(random.nextInt()) % 100;
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = new Blog();
			blog.setId(randomId);
			blog.setAuthor("author");
			blog.setContent("content");
			mapper.insert(blog);
			logger.info("insert:{}", blog.toString());
		} finally {
			session.commit();
			session.close();
		}
	}

	// @Test
	public void update() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = mapper.selectById(57);
			logger.info("before update:{}", blog.toString());
			blog.setAuthor("author_new");
			mapper.update(blog);
		} finally {
			session.commit();
			session.close();
		}
	}

	// @Test
	public void delete() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Blog blog = new Blog();
			blog.setId(6);
			mapper.delete(blog);
		} finally {
			session.commit();
			session.close();
		}
	}

	// @Test
	public void deleteById() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			mapper.deleteById(9);
		} finally {
			session.commit();
			session.close();
		}
	}

	// @Test
	public void selectByPage() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			Page page = new Page();
			page.setCurrentPage(2);
			List<Blog> pageblog = mapper.selectByPage(page);
			logger.info("selectByPage_2:{}", pageblog.size());
		} finally {
			session.close();
		}
	}

	@Test
	public void criteria() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BlogMapper mapper = session.getMapper(BlogMapper.class);
			List<Integer> values = new ArrayList<Integer>();
			values.add(1);
			values.add(4);
			values.add(6);
			CriteriaQuery query = new CriteriaQuery();
			query.where(
					Criteria.newCriteria().eq("author", "nike")
							.between("id", 1, 5).in("id", values))
					.asc(new String[] { "id" })
					.desc(new String[] { "content", "author" })
					.setDistinct(true).groupBy("id").groupBy("content");
			List<Blog> pageblog = mapper.query(query);
			logger.info("selectByPage_2:{}", pageblog.size());
		} finally {
			session.close();
		}

	}

}
