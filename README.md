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
#### 3、mapper或者说dao需继承CrudMapper<T>。

```java
public interface BlogMapper extends CrudMapper<Blog> {
  
}
```
这里要注意：
* 泛型需指定为第2步的实体类，表示CrudMapper中的方法只操作此实体对应的表。
* 子类不能重载CrudMapper中的方法。根据mybatis规范，mapper接口中的方法名必须唯一。
* 单表crud交给CrudMapper方法处理，子类只需关注复杂的join查询。复杂查询可以写在xml中（个人推荐）也可以用注解来写，这几种方式都是可以并存的。这里有个[例子](https://github.com/tangxin983/easy-mybatis/blob/master/src/test/java/com/github/tx/mybatis/test/mapper/BlogMapper.xml)

#### 4、使用接口CrudMapper的方法，具体见[测试用例](https://github.com/tangxin983/easy-mybatis/blob/master/src/test/java/com/github/tx/mybatis/test/CrudMapperTest.java)



