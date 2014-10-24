package com.github.tx.mybatis.test;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 
 * @author tangx
 * @since 2014年10月22日
 */

public abstract class AbstractMybatisTest {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected SqlSessionFactory sqlSessionFactory;

	@Before
	public void setUp() throws Exception {
		InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@After
	public void tearDown() throws Exception {
	}
	 
}
