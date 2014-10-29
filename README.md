# easy-mybatis

>make mybatis much easier to use.

* 单表CRUD只需定义接口，不需要任何xml。
* 直观的条件查询api。
* 最易用的物理分页。

## 一、单表CRUD

#### 1、配置插件

```xml
<plugins>
	<plugin interceptor="com.github.tx.mybatis.interceptor.AutoMappingInterceptor" />
</plugins>
```

[例子](https://github.com/tangxin983/easy-mybatis/blob/master/src/test/resources/mybatis-config.xml)

#### 2、实体类

@Table的name属性为空的话取类名为表名，@Column同理。

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
	private Date createDate;

  //getter,setter...
}
```
#### 3、业务mapper接口需继承CrudMapper

```java
public interface BlogMapper extends CrudMapper<Blog> {
  
}
```
这里要注意：
* 泛型必须指定为第2步的实体类，表示CrudMapper中的方法只操作此实体对应的表。
* mybatis规定mapper接口中的方法名必须唯一，所以子类不能重载CrudMapper中的方法。
* 单表crud交给CrudMapper处理，子接口只需关注复杂的join查询。复杂查询可以写在xml中（个人推荐）也可以用注解来写，这几种方式都是可以并存的。[例子](https://github.com/tangxin983/easy-mybatis/blob/master/src/test/java/com/github/tx/mybatis/test/mapper/BlogMapper.xml)

#### 4、条件查询的使用

```java
CriteriaQuery query = new CriteriaQuery();
query.or(Criteria.newCriteria().ge("id", 1))
     .or(Criteria.newCriteria().eq("author", "mike").isNotNull("content"))
     .desc("id");
```
以上语句对应的条件查询sql为
```sql
where (id >= 1)
	or (author = 'mike' and content is not null)
	order by id desc
```

#### 5、使用

具体见[测试用例](https://github.com/tangxin983/easy-mybatis/blob/master/src/test/java/com/github/tx/mybatis/test/CrudMapperTest.java)

## 二、物理分页(可单独使用)

#### 1、配置插件

```xml
<plugins>
	<plugin interceptor="com.github.tx.mybatis.interceptor.CacheKeyInterceptor" />
	<plugin interceptor="com.github.tx.mybatis.interceptor.PageInterceptor" />
</plugins>
```

#### 2、使用

构造一个Page对象作为参数传入即可。currentPage为当前页码，size为每页记录数。

```java
Page page = new Page();
page.setCurrentPage(1);
page.setSize(5);
List<Blog> list = mapper.selectByPage(page);
```
