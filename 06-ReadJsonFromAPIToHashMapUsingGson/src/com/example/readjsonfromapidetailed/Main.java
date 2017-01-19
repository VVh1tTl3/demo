package com.example.readjsonfromapidetailed;

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
	static String sApiMethod = "GET";
	static String sApiURL = "http://www.codingfury.net/training/weathersample/weather.php";
	static boolean exit = false;
	public static void main(String[] args) throws NumberFormatException, IOException {
		do{
		selectWeatherLocation();
		}while(exit != true);
	}

	private static void selectWeatherLocation() throws NumberFormatException, IOException {
		int iOpt;
		sApiURL = "http://www.codingfury.net/training/weathersample/weather.php";
		String bel = "Belfast", dub = "Dublin", lon = "London";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please select the city you wish to display:");
		System.out.println("-------------------------------------------");
		System.out.println("1.) " + bel);
		System.out.println("2.) " + dub);
		System.out.println("3.) " + lon);
		System.out.println("4.) Exit System");
		System.out.print("Option Selected:");
		iOpt = Integer.parseInt(reader.readLine());
		switch(iOpt){
		case 1:sApiURL += "?location="+bel;
		System.out.println("Belfast Selected");
		break;
		case 2:sApiURL +=  "?location="+dub;
		System.out.println("Dublin Selected");
		break;
		case 3:sApiURL +=  "?location="+lon;
		System.out.println("London Selected");
		break;
		case 4:
			System.out.println("System exited");
			exit = true;
			break;
		default:System.out.println("Incorrect Option selected");
		break;
		
		}
		if(iOpt == 1 || iOpt == 2 || iOpt ==3){
		String sJsonString = netGetJSON(sApiMethod, sApiURL);
		Map<String, Object> mLocationsDict = new Gson().fromJson(sJsonString,
				new TypeToken<HashMap<String, Object>>() {
				}.getType());
		printOutWeatherLocations(mLocationsDict);
		}
		
	}

	private static void printOutWeatherLocations(
			Map<String, Object> mLocationsDict) {
		Map<String, Object> locationMap = (Map<String, Object>) mLocationsDict.get("location");
		System.out.println("Location Details");
		System.out.println("---------------------");
		for(String key: locationMap.keySet()){
			System.out.println(key + ": " +locationMap.get(key));
		}
		System.out.println("---------------------");
		System.out.println("Weather Details");
		System.out.println("---------------------");
		
		Map<String, Object> weatherMap = (Map<String, Object>) mLocationsDict.get("weather");
		for(String key: weatherMap.keySet()){
			System.out.println(key + ": " +weatherMap.get(key));
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
