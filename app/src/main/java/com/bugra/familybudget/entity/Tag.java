package com.bugra.familybudget.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;

    private static Map<Integer, Tag> tagMap = new HashMap();

	private int id;
	private String name;
    private String color;
    private String iconText;
    private BigDecimal limit;

    public static Tag parseJSON(JSONObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = jsonObject.toString();

        try {
            return mapper.readValue(jsonInString, Tag.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<Tag> getTagList() {
        List tagList = new ArrayList(tagMap.values());
        Collections.sort(tagList, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                Tag lhsTag = (Tag) lhs;
                Tag rhsTag = (Tag) rhs;
                return lhsTag.getName().compareTo(rhsTag.getName());
            }
        });
        return tagList;
    }

    public static Tag getTag(int id) {
        return tagMap.get(id);
    }

    public static void putTag(int id, Tag tag) {
        tagMap.put(id, tag);
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIconText() {
        return iconText;
    }

    public void setIconText(String iconText) {
        this.iconText = iconText;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

}