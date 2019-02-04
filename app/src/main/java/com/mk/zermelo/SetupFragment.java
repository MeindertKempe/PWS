package com.mk.zermelo;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class SetupFragment extends Fragment {

	private static final String TAG = "SetupFragment";
	private SetupViewModel viewModel;
	private EditText schoolUrlTextEdit;
	private EditText authCodeTextEdit;
	private Button continueSetupButton;
	private TextView errorMessage;

	public static SetupFragment newInstance() {
		return new SetupFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {


		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.setup_fragment, container, false);
		schoolUrlTextEdit = view.findViewById(R.id.schoolUrl);
		authCodeTextEdit = view.findViewById(R.id.authCode);
		continueSetupButton = view.findViewById(R.id.continue_setup);
		errorMessage = view.findViewById(R.id.error_message);

		// onclick add new user
		continueSetupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addUser();
			}
		});


		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		viewModel = ViewModelProviders.of(this).get(SetupViewModel.class);
	}

	private void addUser() {
		String authCode = authCodeTextEdit.getText().toString();
		String schoolUrl = schoolUrlTextEdit.getText().toString();
		boolean error = false;


		// Add new user
		try {
			viewModel.addUser(schoolUrl, authCode);
		} catch (IOException e) {
			// If an error occurs show error message
			Log.e(TAG, "User creation failed");
			errorMessage.setText(R.string.add_user_error);
			error = true;
		}

		// Get parent activity and call goToSchedule method which switches to a different activity
		if (!error) {
			Activity activity = getActivity();
			if (activity instanceof MainActivity) {
				((MainActivity) activity).goToSchedule();
			}
		}
	}
}
