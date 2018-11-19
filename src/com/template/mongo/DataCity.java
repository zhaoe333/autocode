package com.template.mongo;

/**
 * Entity类
 * @author zyl
 * @date 2016-11-2
 */
public class DataCity implements java.io.Serializable {
	
	//
	private java.lang.String code;
	//
	private java.lang.String name;
	//
	private java.lang.String parentCode;
	
	private Double lon;
	
	private Double lat;
    
    public DataCity() {

    }

	public java.lang.String getCode() {
        return this.code;
    }
    public void setCode(java.lang.String code) {
        this.code = code;
    }
	public java.lang.String getName() {
        return this.name;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }
	public java.lang.String getParentCode() {
        return this.parentCode;
    }
    public void setParentCode(java.lang.String parentCode) {
        this.parentCode = parentCode;
    }

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
    
}