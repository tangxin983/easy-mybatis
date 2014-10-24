package com.github.tx.mybatis.test.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author tangx
 * @since 2014年10月22日
 */

@Table(name = "blog")
public class Blog {

	@Id
	private Integer id;

	@Column
	private String author;

	@Column
	private String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	
	public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
