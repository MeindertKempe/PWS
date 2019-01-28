package com.mk.zermelo;

import androidx.room.RoomDatabase;

@androidx.room.Database(version = 1, entities = {Appointment.class, User.class}, exportSchema = false)
public abstract class Database extends RoomDatabase {
	public abstract Dao dao();
}
