package com.github.tx.mybatis.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 将泛型类设置到参数中
 * @author tangx
 * @since 2014年10月24日
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }) })
public class SetGenericParamInterceptor extends BaseInterceptor implements
		Interceptor {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) queryArgs[0];
		final Object parameter = queryArgs[1];
		Class<?> entityClazz = getEntityClass(ms);
		if (entityClazz != null) {
			// 将泛型类加入到参数中供CrudTemplate使用
			if (parameter != null) {
				Map map;
				if (parameter instanceof Map) {
					map = (HashMap) parameter;
					map.put("", entityClazz);
				} else {
					map = new HashMap();
					map.put("1", parameter);
					map.put("2", entityClazz);
				}
				queryArgs[1] = map;
			} else {
				queryArgs[1] = entityClazz;
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
