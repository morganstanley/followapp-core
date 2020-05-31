package com.followapp.core.model;

import java.util.ArrayList;
import java.util.List;

public class Recipient {
    
    private Integer id;

    private String name;

    private String mobile;
    
    private String gender;
    
    private String zone;
    
    private List<Integer> groups;
         
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	
    public List<Integer> getGroups() {
    	if(groups == null)
    		groups = new ArrayList<Integer>();
		return groups;
	}

	public void setGroups(List<Integer> groups) {
		this.groups = groups;
	}	

	@Override
    public String toString() {
        return "Recipient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", gender='" + gender + '\'' +
                ", zone='" + zone + '\'' +
                ", groups='" + groups + '\'' +
                '}';
    }
}
