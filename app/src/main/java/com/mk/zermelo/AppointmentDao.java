package com.mk.zermelo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AppointmentDao {

	@Insert
	void insertAppointment(Appointment appointment);

	@Query("DELETE FROM Appointment where userId = :userId")
	void deleteAllAppointments(int userId);

	// Get any appointments that start between the given timestamps.
	@Query("SELECT * FROM appointment WHERE (userId = :userId) AND " +
			"((start >= :timeStart AND start <= :timeEnd) OR " +
			"([end] >= :timeStart AND [end] <= :timeEnd))")
	LiveData<List<Appointment>> getAppointments(int userId, long timeStart, long timeEnd);
}
