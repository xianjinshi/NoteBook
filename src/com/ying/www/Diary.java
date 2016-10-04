package com.ying.www;

public class Diary {
	private String title;
	private String body;
	private String type;
	private String created;
	private int imageId;
	public Diary(String title,String body,String type,String created,int imageId){
		this.title = title;
		this.body  = body;
		this.type = type;
		this.created = created;
		this.imageId = imageId;
	}
	public String getTitle(){
		return title;
	}
	public String getBody(){
		return body;
	}
	public String getType(){
		return type;
	}
	public String getCreated(){
		return created;
	}
	public int getImageId(){
		return imageId;
	}
}
