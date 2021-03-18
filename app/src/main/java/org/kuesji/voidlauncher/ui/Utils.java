package org.kuesji.voidlauncher.ui;

import android.content.Context;

public class Utils {
	public static float dp2px(Context context, float dp){
		return dp*context.getResources().getDisplayMetrics().density;
	}

	public static int dp2px(Context context, int dp){
		return (int)(dp2px(context,(float)dp));
	}
}
