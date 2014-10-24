package com.github.tx.mybatis.interceptor;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tx.mybatis.util.ReflectUtil;

/**
 * 拦截器基类
 * 
 * @author tangx
 * @since 2014年10月24日
 */

public abstract class BaseInterceptor {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取被拦截的对象(MetaObject包装)
	 * 
	 * @param obj
	 * @return
	 */
	protected MetaObject getMetaObject(Object obj) {
		ObjectFactory objectFactory = new DefaultObjectFactory();
		ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
		MetaObject metaStatementHandler = MetaObject.forObject(obj,
				objectFactory, objectWrapperFactory);
		// 由于目标类可能被多个拦截器拦截，从而形成多次代理，通过以下循环找出原始代理
		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object, objectFactory,
					objectWrapperFactory);
		}
		// 得到原始代理对象的目标类，即StatementHandler实现类
		if (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object, objectFactory,
					objectWrapperFactory);
		}
		return metaStatementHandler;
	}

	/**
	 * 获取mapper对应的泛型类
	 * @param mappedStatement
	 * @return
	 */
	protected Class<?> getEntityClass(MappedStatement mappedStatement) {
		String statementId = mappedStatement.getId();
		// 通过Mapper接口名反射得到对应的泛型
		String namespace = statementId.substring(0, statementId.lastIndexOf("."));
		Class<?> entityClazz;
		try {
			entityClazz = ReflectUtil.getGenricType(Class.forName(namespace));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("cant find entity class");
		}
		return entityClazz;
	}
}
