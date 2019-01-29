package com.mk.zermelo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AppointmentDao {

	@Insert
	public void insertAppointment(Appointment appointment);

	@Query("DELETE FROM Appointment")
	public void deleteAllAppointments();
}
