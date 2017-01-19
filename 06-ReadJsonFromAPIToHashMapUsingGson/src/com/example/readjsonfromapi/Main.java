package com.example.readjsonfromapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {

	public static void main(String[] args) {
		// Phase 1 - perform API Call
		String sApiMethod = "GET";
		String sApiURL = "http://www.codingfury.net/training/weathersample/weather.php";

		String sJsonString = netGetJSON(sApiMethod, sApiURL);

		// Code
		// Phanse 2 - json String into a HashMap

		Map<String, Object> mLocationsDict = new Gson().fromJson(sJsonString,
				new TypeToken<HashMap<String, Object>>() {
				}.getType());
		// Phase 3 - Print out weather
		printOutWeatherLocations(mLocationsDict);

	}

	private static void printOutWeatherLocations(
			Map<String, Object> mLocationsDict) {
		System.out.println(mLocationsDict);

	}

	private static String netGetJSON(String sApiMethod, String sApiURL) {

		// Variables
		String sRequestedJson;
		StringBuilder sbBuilder = new StringBuilder();

		// Code
		try {
			URL url = new URL(sApiURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(sApiMethod);
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed: HTTP error code: "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			while ((sRequestedJson = br.readLine()) != null) {
				sbBuilder.append(sRequestedJson);
			}
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sbBuilder.toString();
	}

}
