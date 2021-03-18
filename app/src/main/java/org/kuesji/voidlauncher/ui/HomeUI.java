package org.kuesji.voidlauncher.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class HomeUI extends RelativeLayout {

	HomePage home;
	AppsPage apps;


	Point home_touch = new Point();
	boolean home_touch_down = false;
	Point apps_touch = new Point();
	boolean apps_touch_down = false;

	@SuppressLint("ClickableViewAccessibility")
	public HomeUI(Context context) {
		super(context);

		home = new HomePage(context);
		home.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		addView(home);

		apps = new AppsPage(context);
		apps.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		apps.setBackgroundColor(Color.argb(150, 0, 0, 0));
		addView(apps);

		home.setOnTouchListener((view, event) -> {

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					home_touch.set((int) event.getX(), (int) event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					home_touch_down = (event.getY() > home_touch.y) ? true : false;
					home_touch.set((int) event.getX(), (int) event.getY());
					apps.setY(event.getY());
					break;
				case MotionEvent.ACTION_UP:
					if ( !home_touch_down ) {
						apps.setY(0);
					} else {
						apps.setY(getHeight());
					}
					break;
			}

			return true;
		});

		apps.setOnTouchListener((view, event) -> {

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					apps_touch.set( (int) event.getX(), (int) event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					apps_touch_down = ( event.getY() > apps_touch.y ) ? true : false;
					apps_touch.set( (int) event.getX(), (int)(apps.getY() + event.getY()));
					apps.setY(apps.getY()+event.getY());
					break;
				case MotionEvent.ACTION_UP:
					if( !apps_touch_down ){
						apps.setY(getHeight());
						apps.scroller.scrollTo(0,0);
					} else {
						apps.setY(0);
					}
					break;
			}

			return true;
		});

		apps.load();
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (changed) {
			apps.setY(getHeight());
		}
	}

	public void onBackPressed() {
		if ( apps.getY() != getHeight() ) {
			apps.setY(getHeight());
			apps.scroller.scrollTo(0,0);
		}
	}
}
