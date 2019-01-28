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

	public User(int id, String apiToken, String url) {
		this.id = id;
		this.apiToken = apiToken;
		this.url = url;
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
