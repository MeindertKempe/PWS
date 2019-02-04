package com.mk.zermelo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
	@Insert
	long insertUser(User user);

	@Query("SELECT * FROM User WHERE id = :userId")
	LiveData<User> getUser(int userId);
}
