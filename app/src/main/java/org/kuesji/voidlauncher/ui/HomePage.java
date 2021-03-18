package org.kuesji.voidlauncher.ui;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePage extends LinearLayout {

	TextView text_clock;
	TextView text_date;
	LinearLayout tmp_hack_for_arrow;

	Runnable task_clock = () -> {
		SimpleDateFormat clock_formatter = new SimpleDateFormat("HH:mm:ss");;
		SimpleDateFormat date_formatter = new SimpleDateFormat("dd MM yyyy");
		Date date = new Date();

		text_clock.setText(clock_formatter.format(date));
		text_date.setText(date_formatter.format(date));
		getHandler().postDelayed(this.task_clock, 1000);
	};

	public HomePage(Context context) {
		super(context);

		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.LEFT | Gravity.TOP);
		setPadding(Utils.dp2px(context,32),Utils.dp2px(context,32),Utils.dp2px(context,32),Utils.dp2px(context,32));

		text_clock = new TextView(context);
		text_clock.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		text_clock.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		text_clock.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 48);
		text_clock.setText("00:00:00");
		text_clock.setTextColor(0xffeeeeee);
		addView(text_clock);

		text_date = new TextView(context);
		text_date.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		text_date.setGravity(Gravity.LEFT | Gravity.TOP);
		text_date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
		text_date.setText("1 july 2020");
		text_date.setTextColor(0xffeeeeee);
		addView(text_date);

		tmp_hack_for_arrow = new LinearLayout(context);
		tmp_hack_for_arrow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		tmp_hack_for_arrow.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
		addView(tmp_hack_for_arrow);
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getHandler().post(task_clock);
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getHandler().removeCallbacks(task_clock);
	}
}
