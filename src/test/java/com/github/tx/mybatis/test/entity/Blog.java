package com.github.tx.mybatis.test.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

@Table(name = "blog")
public class Blog {

	@Id
	private String id;

	@Column
	private String author;

	@Column
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
