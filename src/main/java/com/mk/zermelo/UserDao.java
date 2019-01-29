package com.mk.zermelo;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface UserDao {
	@Insert
	public void insertUser(User user);

}
