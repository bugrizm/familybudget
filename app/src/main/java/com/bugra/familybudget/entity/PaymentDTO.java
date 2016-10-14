package com.bugra.familybudget.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PaymentDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal amount;
	private Date date;
	private Short month;
	private String name;
	private Short year;
    private boolean isMultiple;
    private int installmentAmount;
    private int tagId;

	public PaymentDTO() {
	}

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
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

    public int getTagId() {
        return this.tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public boolean getIsMultiple() {
        return this.isMultiple;
    }

    public void setIsMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    public int getInstallmentAmount() {
        return this.installmentAmount;
    }

    public void setInstallmentAmount(int installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

}