package com.mk.zermelo;


import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(indices = {@Index("id")})
public class User {
	@PrimaryKey(autoGenerate = true)
	private int id;

	private String apiToken;
	private String url;

	public User(String url) {
		this.url = url;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public int getId() {
		return id;
	}

	public String getApiToken() {
		return apiToken;
	}

	public String getUrl() {
		return url;
	}
}
