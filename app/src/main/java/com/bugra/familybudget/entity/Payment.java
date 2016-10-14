package com.bugra.familybudget.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private BigDecimal amount;
	private Date date;
	private Short month;
	private String name;
	private Short year;
	private Integer parentId;
	private Integer tagId;

    public static Payment parseJSON(JSONObject jsonObject) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = jsonObject.toString();

		try {
			return mapper.readValue(jsonInString, Payment.class);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Short getMonth() {
		return this.month;
	}

	public void setMonth(Short month) {
		this.month = month;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getYear() {
		return this.year;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentPayment) {
		this.parentId = parentId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
}