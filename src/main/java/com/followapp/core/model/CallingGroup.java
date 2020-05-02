package com.followapp.core.model;

import java.util.ArrayList;
import java.util.List;

public class CallingGroup {
    
    private Integer id;

    private String name;

    private String description;
    
    private List<Integer> recipients;
         
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public List<Integer> getRecipients() {
    	if(recipients == null)
    		recipients = new ArrayList<Integer>();
		return recipients;
	}

	public void setRecipients(List<Integer> recipients) {
		this.recipients = recipients;
	}

	@Override
    public String toString() {
        return "CallingGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", Recipients='" + recipients + '\'' +
                '}';
    }
}
