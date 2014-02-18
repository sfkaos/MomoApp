package com.winraguini.momoapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
//import com.parse.integratingfacebooktutorial.R;

public class FacebookApplication extends Application {

	static final String TAG = "MyApp";

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "DkkHOlOQkgY380RzJYysUGayxaNgHitwPgfz7zaO",
				"lwdIaRNMSkxZhLm8DWNFCRYFwCgfAEarH4silhgn");

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));

	}

}
