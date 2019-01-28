package com.mk.zermelo;

import androidx.room.Insert;
import androidx.room.Query;

@androidx.room.Dao
public interface Dao {
	@Insert
	public void insertUser(User user);

	@Insert
	public void insertAppointment(Appointment appointment);

	@Query("DELETE FROM Appointment")
	public void deleteAllAppointments();
}
