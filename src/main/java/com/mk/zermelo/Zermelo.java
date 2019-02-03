package com.mk.zermelo;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class Zermelo {
	private String schoolURL;
	private static final String api = ".zportal.nl/api/v3";
	private String apiToken;

	public Zermelo(User user) {
		// Create proper url
		this.schoolURL = "https://" + user.getUrl() + api;

		// Save api token if it exists
		if (user.getApiToken() != null) {
			this.setApiToken(user.getApiToken());
		}
	}

	private static synchronized String requestZermeloApiToken(String schoolURL, String authCode) throws IOException, JSONException {

		authCode = authCode.replace(" ", "");

		String url = "https://" + schoolURL + api + "/oauth/token";
		String postData = "grant_type=authorization_code&code=" + authCode;

		URL zermeloURL;

		// Try to convert string to URL object
		zermeloURL = new URL(url);

		// Do get request
		JSONObject json = zermeloGetRequestPost(zermeloURL, postData);

		return json.getString("access_token");
	}

	private synchronized JSONObject requestZermeloAppointments(long startTime, long endTime) throws IOException, JSONException {

		String url = schoolURL + "/appointments";
		String getData = "user=~me" +
				"&start=" + startTime +
				"&end=" + endTime +
				"&access_token=" + getApiToken();

		URL zermeloURL;

		// Try to convert string to URL object
		zermeloURL = new URL(url);

		// Do get request
		JSONObject json = zermeloGetRequestGet(zermeloURL, getData);

		return json;
	}

	private static synchronized JSONObject zermeloGetRequestPost(URL url, String postData) throws IOException, JSONException {

		// Create url connection and cast it to a https connection
		HttpsURLConnection httpscon = (HttpsURLConnection) url.openConnection();

		// Indicate we will send and receive data
		httpscon.setDoInput(true);
		httpscon.setDoOutput(true);

		// Convert post data to raw bytes and get the length of the byte array
		byte[] bytes = postData.getBytes(StandardCharsets.UTF_8);
		int postLength = bytes.length;

		// Set length of bytes
		httpscon.setFixedLengthStreamingMode(postLength);

		// Set content type and encoding for the request
		httpscon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		// Send data to url
		OutputStream output = httpscon.getOutputStream();
		output.write(bytes);

		// Get the returned data stream
		InputStream inputStream = httpscon.getInputStream();

		// https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
		// todo this
		Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
		String result = scanner.next();

		// Close input stream
		inputStream.close();

		// Parse Json
		JSONObject json = new JSONObject(result);

		return json;
	}

	private static synchronized JSONObject zermeloGetRequestGet(URL zermeloUrl, String getData) throws IOException, JSONException {

		URL url = new URL(zermeloUrl + "?" + getData);

		// Create url connection and cast it to a https connection
		HttpsURLConnection httpscon = (HttpsURLConnection) url.openConnection();

		httpscon.setDoInput(true);
		httpscon.setDoOutput(false);

		// Set content type and encoding for the request
		httpscon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		// Get the returned data stream
		InputStream inputStream = httpscon.getInputStream();

		// Convert input stream to string
		Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
		String result = scanner.next();

		// Close input stream
		inputStream.close();

		// Parse Json
		JSONObject json = new JSONObject(result);

		return json;
	}

	public String getSchoolURL() {
		return schoolURL;
	}

	public void setSchoolURL(String schoolURL) {
		this.schoolURL = schoolURL;
	}

	public static String getApi() {
		return api;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public String requestApiToken(String schoolURL, String accessCode) {
		String result = null;
		try {
			result = new requestAccessTokenAsyncTask(schoolURL, accessCode).execute().get();
		} catch (InterruptedException | ExecutionException e) {
		}

		return result;
	}

	public JSONObject requestAppointments(long startTime, long endTime) {
		JSONObject result = null;
		try {
			result = new requestAppointmentsAsyncTask(startTime, endTime, this).execute().get();
		} catch (InterruptedException | ExecutionException e) {
		}

		return result;
	}


	public static class requestAccessTokenAsyncTask extends AsyncTask<Void, Void, String> {
		private String schoolUrl;
		private String accessCode;

		requestAccessTokenAsyncTask(String schoolUrl, String accessCode) {
			this.schoolUrl = schoolUrl;
			this.accessCode = accessCode;
		}

		@Override
		protected String doInBackground(Void... voids) {
			String accessToken = null;
			try {
				accessToken = Zermelo.requestZermeloApiToken(schoolUrl, accessCode);
			} catch (JSONException e) {
			} catch (IOException e) {
			}
			return accessToken;
		}
	}

	public static class requestAppointmentsAsyncTask extends AsyncTask<Void, Void, JSONObject> {
		private long startTime;
		private long endTime;
		private Zermelo zermelo;

		requestAppointmentsAsyncTask(long startTime, long endTime, Zermelo zermelo) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.zermelo = zermelo;
		}

		@Override
		protected JSONObject doInBackground(Void... voids) {
			JSONObject json = null;
			try {
				json = zermelo.requestZermeloAppointments(startTime, endTime);
			} catch (JSONException e) {
			} catch (IOException e) {
			}
			return json;
		}
	}
}
