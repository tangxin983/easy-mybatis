package com.github.tx.mybatis.interceptor;

import java.util.Properties;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import com.github.tx.mybatis.entity.Page;

/**
 * 在cacheKey中加入分页属性，避免在开启缓存的情况下只返回第一页数据的错误。此插件需配合PageInterceptor一起使用
 * 
 * @author tangx
 * @since 2014年10月25日
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }) })
public class CacheKeyInterceptor extends BaseInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		Object parameterObject = args[1];
		Page page = retrievePageFromParam(parameterObject);
		if (page != null) {
			RowBounds rowBounds = new RowBounds(page.getCurrentPage(), page.getSize());
			args[2] = rowBounds;
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		MetaObject metaObject = getMetaObject(target);
		if (metaObject.getOriginalObject() instanceof CachingExecutor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
