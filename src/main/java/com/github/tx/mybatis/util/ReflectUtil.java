package com.github.tx.mybatis.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * 反射工具类
 * @author tangx
 * @since 2014年10月22日
 */

public class ReflectUtil {

	// 用于存放各实体的属性名-列名关系
	private static final Map<Class<?>, Map<String, String>> columnMap = new HashMap<Class<?>, Map<String, String>>();

	/**
	 * 获取实体表名。实体需定义@Table(name)，如果没有name则取类名为表名
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table != null) {
			if (StringUtils.isNotBlank(table.name())) {
				return table.name();
			} else {
				return clazz.getSimpleName();
			}
		} else {
			throw new RuntimeException("cant find @Table annotation");
		}

	}

	/**
	 * 获取主键字段名
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getIdFieldName(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class))
				return field.getName();
		}
		// 没有找到id，向上找父类的id
		if (clazz.getSuperclass() != null) {
			return getParentIdFieldName(clazz.getSuperclass());
		} else {
			throw new RuntimeException("cant find @Id annotation");
		}
	}

	/**
	 * 获取父类中的主键字段
	 * 
	 * @param clazz
	 * @return
	 */
	private static String getParentIdFieldName(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class))
				return field.getName();
		}
		// 继续向上找父类id
		if (clazz.getSuperclass() != null) {
			return getParentIdFieldName(clazz.getSuperclass());
		} else {
			throw new RuntimeException("cant find @Id annotation");
		}
	}

	/**
	 * 获取主键列名
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getIdColumnName(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				if (field.isAnnotationPresent(Column.class)) {
					Column c = field.getAnnotation(Column.class);
					if (StringUtils.isNotBlank(c.name())) {
						return c.name();
					}
				}
				return field.getName();
			}
		}
		if (clazz.getSuperclass() != null) {
			return getParentIdColumnName(clazz.getSuperclass());
		} else {
			throw new RuntimeException("cant find @Id annotation");
		}
	}

	/**
	 * 查询父类的主键列名
	 * 
	 * @param clazz
	 * @return
	 */
	private static String getParentIdColumnName(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				if (field.isAnnotationPresent(Column.class)) {
					Column c = field.getAnnotation(Column.class);
					if (StringUtils.isNotBlank(c.name())) {
						return c.name();
					}
				}
				return field.getName();
			}
		}
		if (clazz.getSuperclass() != null) {
			return getParentIdColumnName(clazz.getSuperclass());
		} else {
			throw new RuntimeException("cant find @Id annotation");
		}
	}

	/**
	 * 计算实体中标记为@Column的属性，以属性名为key，数据库字段名为value，放到Map中(这里排除@Id字段，即使该字段也有@Column)
	 */
	public static void caculationColumnList(Class<?> clazz) {
		if (columnMap.containsKey(clazz)) {
			return;
		}
		Map<String, String> columnDefs = new HashMap<String, String>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				if (field.isAnnotationPresent(Id.class)) {
					continue;
				}
				columnDefs.put(field.getName(), getColumnName(field));
			}
		}
		// 获取父类属性
		if (clazz.getSuperclass() != null) {
			fields = clazz.getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					if (field.isAnnotationPresent(Id.class)) {
						continue;
					}
					columnDefs.put(field.getName(), getColumnName(field));
				}
			}
		}
		columnMap.put(clazz, columnDefs);
	}
	
	/**
	 * 根据类获取对应的属性名-列名关系
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Map<String, String> getFieldColumnMapping(Class<?> clazz) {
		caculationColumnList(clazz);
		return columnMap.get(clazz);
	}

	/**
	 * 获取field对应的列名
	 * 
	 * @param field
	 * @return
	 */
	private static String getColumnName(Field field) {
		Column c = field.getAnnotation(Column.class);
		if (!StringUtils.isBlank(c.name())) {
			return c.name();
		} else {
			return field.getName();
		}
	}

	/**
	 * 获取更新语句的SQL(忽略空值)
	 * 
	 * @param obj
	 * @return
	 */
	public static String getUpdateSQL(Object obj) {
		caculationColumnList(obj.getClass());
		StringBuilder sb = new StringBuilder();
		Map<String, String> field2Column = columnMap.get(obj.getClass());
		int i = 0;
		for (String fieldName : field2Column.keySet()) {
			if (isFieldNull(obj, fieldName)) {
				continue;
			}
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append(field2Column.get(fieldName)).append("=#{")
					.append(fieldName).append('}');
		}
		return sb.toString();
	}

	/**
	 * 获取要插入的列名 (忽略空值)
	 * 
	 * @param obj
	 * @return
	 */
	public static String insertColumnNameList(Object obj) {
		caculationColumnList(obj.getClass());
		StringBuilder sb = new StringBuilder();
		Map<String, String> columnDefs = columnMap.get(obj.getClass());
		int i = 0;
		for (String fieldName : columnDefs.keySet()) {
			if (isFieldNull(obj, fieldName)) {
				continue;
			}
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append(columnDefs.get(fieldName));
		}
		return sb.toString();
	}

	/**
	 * 获取要插入的字段 (忽略空值)
	 * 
	 * @param obj
	 * @return
	 */
	public static String insertFieldNameList(Object obj) {
		caculationColumnList(obj.getClass());
		StringBuilder sb = new StringBuilder();
		Map<String, String> columnDefs = columnMap.get(obj.getClass());
		int i = 0;
		for (String fieldName : columnDefs.keySet()) {
			if (isFieldNull(obj, fieldName)) {
				continue;
			}
			if (i++ != 0) {
				sb.append(',');
			}
			sb.append("#{").append(fieldName).append('}');
		}
		return sb.toString();
	}
	
	/**
	 * 获得mapper接口定义中声明的第一个泛型参数的类型(泛型须定义在父接口),如无法找到返回null
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getGenricType(final Class clazz) {
		return getGenricType(clazz, 0);
	}

	/**
	 * 获得mapper接口定义中声明的泛型参数的类型(泛型须定义在父接口),如无法找到返回null
	 * @param clazz
	 * @param index
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getGenricType(final Class clazz, final int index) {
		Type[] genTypes = clazz.getGenericInterfaces();
		if(genTypes.length == 0){
			return null;
		}
		Type type = genTypes[0];
		if (!(type instanceof ParameterizedType)) {
			return null;
		}
		Type[] params = ((ParameterizedType) type).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return null;
		}
		if (!(params[index] instanceof Class)) {
			return null;
		}
		return (Class) params[index];
	}

	/**
	 * 判断字段值是否为空
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	private static boolean isFieldNull(Object obj, String fieldName) {
		try {
			return FieldUtils.readField(obj, fieldName, true) == null ? true
					: false;
		} catch (Exception e) {
			throw new RuntimeException("isFieldNull error");
		}
	}
}
