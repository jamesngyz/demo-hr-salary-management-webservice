package com.jamesngyz.demo.salarymanagement.common;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class OffsetPageable implements Pageable, Serializable {
	
	private static final long serialVersionUID = 2124131735113788956L;
	
	private final long offset;
	private final int limit;
	private final Sort sort;
	
	public OffsetPageable(Long offset, Integer limit, Sort sort) {
		offset = offset == null ? 0 : offset;
		limit = limit == null ? 0 : limit;
		if (offset < 0) {
			throw new IllegalArgumentException("Offset index must not be less than zero");
		}
		if (limit < 0) {
			throw new IllegalArgumentException("Limit must not be less than zero");
		}
		this.offset = offset;
		this.limit = limit == 0 ? Integer.MAX_VALUE : limit;
		this.sort = sort;
	}
	
	public OffsetPageable(Long offset, Integer limit) {
		this(offset, limit, Sort.by("id"));
	}
	
	public OffsetPageable() {
		this(0L, 0);
	}
	
	@Override
	public int getPageNumber() {
		return (int) (offset / limit);
	}
	
	@Override
	public int getPageSize() {
		return limit;
	}
	
	@Override
	public long getOffset() {
		return offset;
	}
	
	@Override
	public Sort getSort() {
		return sort;
	}
	
	@Override
	public Pageable next() {
		return new OffsetPageable(getOffset() + getPageSize(), getPageSize(), getSort());
	}
	
	public OffsetPageable previous() {
		return hasPrevious() ? new OffsetPageable(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
	}
	
	@Override
	public Pageable previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}
	
	@Override
	public Pageable first() {
		return new OffsetPageable(0L, getPageSize(), getSort());
	}
	
	@Override
	public Pageable withPage(int i) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean hasPrevious() {
		return offset > limit;
	}
	
}
