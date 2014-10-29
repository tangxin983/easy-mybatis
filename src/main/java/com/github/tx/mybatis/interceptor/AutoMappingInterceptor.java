package com.github.tx.mybatis.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.github.tx.mybatis.util.Constants;
import com.github.tx.mybatis.util.ReflectUtil;

/**
 * 自动生成泛型resultMap，并将泛型类设置到参数中
 * 
 * @author tangx
 * @since 2014年10月24日
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }) })
public class AutoMappingInterceptor extends BaseInterceptor implements
		Interceptor {

	private static final String GENERATE_RESULTMAP_NAME = "GenerateResultMap";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] queryArgs = invocation.getArgs();
		MappedStatement ms = (MappedStatement) queryArgs[0];
		Object parameter = queryArgs[1];
		String msId = ms.getId();
		String id = msId.substring(msId.lastIndexOf(".") + 1);// mapper方法名
		String namespace = msId.substring(0, msId.lastIndexOf("."));// mapper类名
		Class<?> entityClazz = ReflectUtil.getEntityClass(namespace);// 泛型实体类对象
		Method mapperMethod = ReflectUtil.getMapperMethod(namespace, id);
		if (ReflectUtil.isAutoMapping(mapperMethod) && entityClazz != null) {
			// 将泛型类加入到参数中供CrudTemplate使用
			if (parameter != null) {
				Map map;
				if (parameter instanceof Map) {
					map = (Map) parameter;
					map.put(Constants.CLASS_KEY, entityClazz);
				} else {
					map = new ParamMap();
					map.put(Constants.PARA_KEY, parameter);
					map.put(Constants.CLASS_KEY, entityClazz);
				}
				queryArgs[1] = map;
			} else {
				queryArgs[1] = entityClazz;
			}
			if (ReflectUtil.isGenerateResultMap(mapperMethod)) {
				// 自动生成resultMap
				List<ResultMap> resultMaps = getResultMap(namespace, entityClazz,
						ms.getConfiguration());
				MetaObject metaMappedStatement = getMetaObject(ms);
				metaMappedStatement.setValue("resultMaps", resultMaps);
			}
		}
		return invocation.proceed();
	}

	/**
	 * 生成泛型对应的ResultMap
	 * 
	 * @param namespace
	 * @param clazz
	 * @param conf
	 * @return
	 */
	private List<ResultMap> getResultMap(String namespace, Class<?> clazz,
			Configuration conf) {
		String dynamicResultMapId = namespace + "." + GENERATE_RESULTMAP_NAME;
		ResultMap dynamicResultMap = buildResultMap(dynamicResultMapId, clazz,
				conf);
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		resultMaps.add(dynamicResultMap);
		return Collections.unmodifiableList(resultMaps);
	}

	/**
	 * 构建ResultMap对象
	 * 
	 * @param id
	 * @param clazz
	 * @param configuration
	 * @return
	 */
	private ResultMap buildResultMap(String id, Class<?> clazz,
			Configuration configuration) {
		// 判断是否已经存在缓存里
		if (configuration.hasResultMap(id)) {
			return configuration.getResultMap(id);
		}
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		// 处理id列
		if (ReflectUtil.getIdFieldName(clazz) != null) {
			Class<?> idType = resolveResultJavaType(clazz,
					ReflectUtil.getIdFieldName(clazz), null);
			List<ResultFlag> flags = new ArrayList<ResultFlag>();
			flags.add(ResultFlag.ID);
			resultMappings.add(buildResultMapping(configuration,
					ReflectUtil.getIdFieldName(clazz),
					ReflectUtil.getIdColumnName(clazz), idType, flags));
		}
		// 处理普通列
		Map<String, String> map = ReflectUtil.getFieldColumnMapping(clazz);
		for (String key : map.keySet()) {
			Class<?> columnTypeClass = resolveResultJavaType(clazz, key, null);
			resultMappings.add(buildResultMapping(configuration, key,
					map.get(key), columnTypeClass, null));
		}
		// 构建ResultMap
		ResultMap.Builder resultMapBuilder = new ResultMap.Builder(
				configuration, id, clazz, resultMappings);
		ResultMap rm = resultMapBuilder.build();
		// 放到缓存中
		configuration.addResultMap(rm);
		return rm;
	}

	/**
	 * 构建ResultMapping对象
	 * 
	 * @param configuration
	 * @param property
	 * @param column
	 * @param javaType
	 * @param flags
	 * @return
	 */
	private ResultMapping buildResultMapping(Configuration configuration,
			String property, String column, Class<?> javaType,
			List<ResultFlag> flags) {
		ResultMapping.Builder builder = new ResultMapping.Builder(
				configuration, property, column, javaType);
		builder.flags(flags == null ? new ArrayList<ResultFlag>() : flags);
		builder.composites(new ArrayList<ResultMapping>());
		builder.notNullColumns(new HashSet<String>());
		return builder.build();
	}

	/**
	 * copy from mybatis sourceCode
	 * 
	 * @param resultType
	 * @param property
	 * @param javaType
	 * @return
	 */
	private Class<?> resolveResultJavaType(Class<?> resultType,
			String property, Class<?> javaType) {
		if (javaType == null && property != null) {
			try {
				MetaClass metaResultType = MetaClass.forClass(resultType);
				javaType = metaResultType.getSetterType(property);
			} catch (Exception e) {
				// ignore, following null check statement will deal with the
				// situation
			}
		}
		if (javaType == null) {
			javaType = Object.class;
		}
		return javaType;
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
