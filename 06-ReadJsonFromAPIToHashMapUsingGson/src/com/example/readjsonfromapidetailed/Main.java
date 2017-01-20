package com.example.readjsonfromapidetailed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {
	static String sApiMethod = "GET";
	static String sApiURL = "http://www.codingfury.net/training/weathersample/weather.php";
	static boolean exit = false;

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		do {
			selectWeatherLocation();
		} while (exit != true);
	}

	private static void selectWeatherLocation() throws NumberFormatException,
			IOException {

		int iOpt, loopCount = 1;

		sApiURL = "http://www.codingfury.net/training/weathersample/weather.php";
		String sJsonString = netGetJSON(sApiMethod, sApiURL);
		Map<String, Object> oLocationsDict = new Gson().fromJson(sJsonString,
				new TypeToken<HashMap<String, Object>>() {
				}.getType());
		System.out.println("Please select the city you wish to display:");
		System.out.println("-------------------------------------------");
		ArrayList<String> locationsArray = (ArrayList<String>) oLocationsDict
				.get("locations");
		for (String key : locationsArray) {
			System.out.println(loopCount + ".) " + key);
			loopCount++;
		}
		System.out.println(loopCount + ".) Exit System");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("Option Selected:");
		iOpt = Integer.parseInt(reader.readLine());
		if (iOpt == loopCount) {
			System.out.println("System Exited");
			exit = true;
		} else if (iOpt < loopCount) {
			for (String str : locationsArray) {
				if (str.equals(locationsArray.get(iOpt - 1))) {
					sApiURL += "?location=" + str;
					sJsonString = netGetJSON(sApiMethod, sApiURL);
					Map<String, Object> mLocationsDict = new Gson().fromJson(
							sJsonString,
							new TypeToken<HashMap<String, Object>>() {
							}.getType());
					printOutWeatherLocations(mLocationsDict);
				}
			}
		} else {
			System.out.println("Wrong option selected");
			return;
		}

	}

	private static void printOutWeatherLocations(
			Map<String, Object> mLocationsDict) {
		Map<String, Object> locationMap = (Map<String, Object>) mLocationsDict
				.get("location");
		System.out.println("Location Details");
		System.out.println("---------------------");
		for (String key : locationMap.keySet()) {
			System.out.println(key.toUpperCase() + ": " + locationMap.get(key));
		}
		System.out.println("---------------------");
		System.out.println("Weather Details");
		System.out.println("---------------------");

		Map<String, Object> weatherMap = (Map<String, Object>) mLocationsDict
				.get("weather");
		for (String key : weatherMap.keySet()) {
			System.out.println(key.toUpperCase() + ": " + weatherMap.get(key));
		}
		System.out.println("---------------------");

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
