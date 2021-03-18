package org.kuesji.voidlauncher.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppsPage extends LinearLayout {

	ScrollView scroller;
	LinearLayout content;
	EditText search_field;

	private Runnable task_load = ()->{
		Context context = getContext();

		PackageManager pm = context.getPackageManager();
		Intent query = new Intent(Intent.ACTION_MAIN);
		query.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> resolves = pm.queryIntentActivities(query,PackageManager.MATCH_ALL|PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS)
			.stream()
		  .sorted((x,y)->
		  	x.loadLabel(pm).toString().compareToIgnoreCase(y.loadLabel(pm).toString())
			).collect(Collectors.toList());

		List<View> views = new ArrayList<>();
		for( ResolveInfo resolve : resolves ){
			views.add(new AppView(context).load(pm,resolve));
		}

		getHandler().post(()->{
			content.removeAllViews();
			for( View view : views ){
				content.addView(view);
			}
		});

	};

	public AppsPage(Context context) {
		super(context);

		int width = getResources().getDisplayMetrics().widthPixels - Utils.dp2px(context,24);
		int height = getResources().getDisplayMetrics().heightPixels - Utils.dp2px(context,12);

		setOrientation(VERTICAL);
		setGravity(Gravity.START|Gravity.BOTTOM);
		setPadding(Utils.dp2px(context,12),0,Utils.dp2px(context,12),Utils.dp2px(context,12));

		setOnTouchListener((view,event) -> true);

		scroller = new ScrollView(context);
		scroller.setLayoutParams(new LayoutParams(width - width/10, height / 10 * 9));
		scroller.setFillViewport(true);
		scroller.setVerticalScrollBarEnabled(false);
		scroller.setVerticalFadingEdgeEnabled(false);
		scroller.setHorizontalScrollBarEnabled(false);
		scroller.setHorizontalFadingEdgeEnabled(false);
		scroller.setOverScrollMode(OVER_SCROLL_NEVER);
		addView(scroller);

		content = new LinearLayout(context);
		content.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		content.setOrientation(VERTICAL);
		content.setGravity(Gravity.START|Gravity.BOTTOM);
		scroller.addView(content);

		search_field = new EditText(context);
		search_field.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dp2px(context,48)));
		search_field.setBackgroundColor(0xffeeeeee);
		search_field.setTextColor(0xff000000);
		search_field.setHintTextColor(0xff000000);
		search_field.setHint("search");
		search_field.setSingleLine(true);
		addView(search_field);

		search_field.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String query = s.toString().toLowerCase();
				for( int i = 0; i < content.getChildCount(); i++){
					if( content.getChildAt(i) instanceof AppView ){
						AppView v = (AppView) content.getChildAt(i);

						if( query.equals("") || v.label.getText().toString().toLowerCase().indexOf(query) != -1 ){
							v.setVisibility(View.VISIBLE);
						} else {
							v.setVisibility(View.GONE);
						}
					}
				}
			}

			public void afterTextChanged(Editable s) { }
		});
	}

	public void load(){
		content.removeAllViews();

		TextView txt_loading = new TextView(getContext());
		txt_loading.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		txt_loading.setGravity(Gravity.START|Gravity.TOP);
		txt_loading.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		txt_loading.setTextColor(0xffeeeeee);
		txt_loading.setText("loading");
		content.addView(txt_loading);

		new Thread(task_load).start();
	}

	private class AppView extends LinearLayout {

		public String _package;
		public String _class;
		public ImageView logo;
		public TextView label;

		public AppView(Context context){
			super(context);

			int refWidth = getResources().getDisplayMetrics().widthPixels - Utils.dp2px(context,64);
			int refHeight = getResources().getDisplayMetrics().heightPixels - Utils.dp2px(context,64);

			MarginLayoutParams lp = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.bottomMargin = Utils.dp2px(context,24);
			setLayoutParams(lp);
			setOrientation(HORIZONTAL);
			setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);

			logo = new ImageView(context);
			logo.setLayoutParams(new LayoutParams(refWidth/10,refWidth/10));
			logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			addView(logo);

			label = new TextView(context);
			label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			label.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
			label.setPadding(refWidth / 100 * 5,0,0,0);
			label.setTextColor(0xffeeeeee);
			addView(label);

			setOnClickListener((v)->{
				if( _package != null && _class != null ) {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName(_package, _class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
					getContext().startActivity(intent);
				}

				AppsPage.this.setY(getResources().getDisplayMetrics().heightPixels);

				scroller.scrollTo(0,0);
				search_field.setText("");
				search_field.clearFocus();
			});

			setOnLongClickListener((v)->{
				if( _package != null ) {
					Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intent.setData(Uri.parse("package:" + _package));
					getContext().startActivity(intent);
				}

				AppsPage.this.setY(getResources().getDisplayMetrics().heightPixels);

				scroller.scrollTo(0,0);
				search_field.setText("");
				search_field.clearFocus();

				return true;
			});
		}

		public AppView load(PackageManager pm, ResolveInfo resolveInfo){
			_package = resolveInfo.activityInfo.packageName;
			_class = resolveInfo.activityInfo.name;

			logo.setImageDrawable(resolveInfo.loadIcon(pm));
			label.setText(resolveInfo.loadLabel(pm));

			return this;
		}
	}
}
