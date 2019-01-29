package com.mk.zermelo;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(version = 1, entities = {Appointment.class, User.class}, exportSchema = false)
public abstract class Database extends RoomDatabase {

	// Singleton pattern
	private static Database instance;

	public static synchronized Database getInstance(Context context) {
		if(instance == null) {
			instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "Database").fallbackToDestructiveMigration().build();
		}
		return  instance;
	}

	public abstract UserDao userDao();
	public abstract AppointmentDao appointmentDao();
}
