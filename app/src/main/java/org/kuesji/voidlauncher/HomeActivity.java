package org.kuesji.voidlauncher;

import android.app.Activity;
import android.os.Bundle;
import org.kuesji.voidlauncher.ui.HomePage;
import org.kuesji.voidlauncher.ui.HomeUI;

public class HomeActivity extends Activity {

	HomeUI ui;

	protected void onCreate(Bundle savedState){
		super.onCreate(savedState);

		ui = new HomeUI(this);

		setContentView(ui);
	}


	public void onBackPressed() {
		ui.onBackPressed();
	}
}
