package com.github.tx.mybatis.entity;

/**
 * 分页参数
 * 
 * @author tangx
 * @since 2014年10月23日
 */

public class Page {

	public static final int DEFAULT_SIZE = 10;

	/** 每页显示几条 */
	private int size;

	/** 数据库总记录数 */
	private int recordsTotal;

	/** 总页数 */
	private int pageTotal;
	
	/** 当前页头条记录序号(序号从0开始) */
	private int start;

	/** 当前页 */
	private int currentPage;

	public Page() {
		this.currentPage = 1;
		this.size = DEFAULT_SIZE;
	}

	public Page(int currentPage, int size) {
		this.currentPage = currentPage;
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
