package com.mk.zermelo;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.IOException;

public class SetupViewModel extends AndroidViewModel {
	private Repository repository;

	public SetupViewModel(@NonNull Application application) {
		super(application);

		// Get repository
		repository = new Repository(application);
	}

	public void addUser(String schoolUrl, String authCode) throws IOException {
		// Add user to database
		repository.addUser(schoolUrl, authCode);

	}
}
