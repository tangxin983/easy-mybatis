package com.github.tx.mybatis.interceptor;

import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tx.mybatis.entity.Page;

/**
 * 拦截器基类
 * 
 * @author tangx
 * @since 2014年10月24日
 */

public abstract class BaseInterceptor {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final ObjectFactory OBJECT_FACTORY = new DefaultObjectFactory();

	private static final ObjectWrapperFactory OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	/**
	 * 获取被拦截的原始对象(MetaObject包装)
	 * 
	 * @param obj
	 * @return
	 */
	protected MetaObject getMetaObject(Object obj) {
		MetaObject metaStatementHandler = MetaObject.forObject(obj,
				OBJECT_FACTORY, OBJECT_WRAPPER_FACTORY);
		// 由于目标类可能被多个拦截器拦截，从而形成多次代理，通过以下循环找出原始代理
		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object, OBJECT_FACTORY,
					OBJECT_WRAPPER_FACTORY);
		}
		// 得到原始代理对象的目标类
		if (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object, OBJECT_FACTORY,
					OBJECT_WRAPPER_FACTORY);
		}
		return metaStatementHandler;
	}
 
	/**
	 * 获取参数中的page对象，如果找不到则返回null
	 * @param paramObj
	 * @return
	 */
	protected Page retrievePageFromParam(Object paramObj) {
		Page page = null;
		if (paramObj != null) {
			if (paramObj instanceof Page) {
				page = (Page) paramObj;
			} else if (paramObj instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) paramObj;
				for (Object key : map.keySet()) {
					Object value = map.get(key);
					if (value instanceof Page && (value != null)) {
						page = (Page) value;
					}
				}
			}
		}
		return page;
	}
}
