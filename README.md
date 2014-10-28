# easy-mybatis

>make mybatis much easier to use.

* 单表crud只需定义接口，不需要任何xml。
* 直观的条件查询api。
* 简单易用的物理分页。

## 一、单表crud:

#### 1、配置mybatis插件:

```xml
<plugins>
	<plugin interceptor="com.github.tx.mybatis.interceptor.AutoMappingInterceptor" />
</plugins>
```

[mybatis配置文件例子](https://github.com/tangxin983/easy-mybatis/blob/master/src/test/resources/mybatis-config.xml)

#### 2、注解实体类:

```java
@Table(name = "blog")
public class Blog {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column
	private String author;

	@Column
	private String content;
	
	@Column(name = "create_date")
	private String createDate;

  //getter,setter...
}
```
#### 3、继承接口CrudMapper<T>，注意泛型T必须指定为第2步定义的实体类型

```java
public interface BlogMapper extends CrudMapper<Blog> {
  
}
```

#### 4、使用接口的crud方法，具体见[测试用例](https://github.com/tangxin983/easy-mybatis/blob/master/src/test/java/com/github/tx/mybatis/test/CrudMapperTest.java)



