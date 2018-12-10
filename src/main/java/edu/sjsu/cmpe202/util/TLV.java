package edu.sjsu.cmpe202.util;

public class TLV {
	
	private String tag;
	private int length;
	private Object value;
	
	public TLV() {
		super();
	}
	
	public TLV(String tag, int length, Object value) {
		super();
		setTag(tag);
		setLength(length);
		setValue(value);
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

}
