package com.smart.unitconverter.Home;

import java.util.ArrayList;


import com.smart.unitconverter.Adapter.ConverterForceAdapter;
import com.smart.unitconverter.R;
import com.smart.unitconverter.dslv.DragSortListView;
import com.smart.unitconverter.objects.MyForceUnitConverter;
import com.smart.unitconverter.objects.MyForceUnitConverter.MyUnit;
import com.smart.unitconverter.quickaction.ActionItem;
import com.smart.unitconverter.quickaction.QuickAction;
import com.smart.unitconverter.Utils.Constant;
import com.smart.unitconverter.Utils.Debug;
import com.smart.unitconverter.Utils.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UnitForceConverterActivity extends SlidingBaseFragmentActivity {

	DragSortListView lvUnitConverter;
	ConverterForceAdapter mAdapter;

	TextView tvUnitSelVal;
	TextView tvUnitValueVal;

	MyForceUnitConverter converter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.unit_converter);

		initSlidingMenu();
		init();
		initAd(R.id.adLayout);
	}

	private void init() {
		converter = new MyForceUnitConverter(getActivity());

		TextView tvTitleText = (TextView) findViewById(R.id.tvTitleText);
		tvTitleText.setTypeface(Utils.getBold(getActivity()));
		tvTitleText.setText(R.string.title_unit);

		Button btnMenu = (Button) findViewById(R.id.btnMenu);
		btnMenu.setOnClickListener(menuClickListener);

		TextView tvUnitSelKey = (TextView) findViewById(R.id.tvUnitSelKey);
		tvUnitSelKey.setTypeface(Utils.getBold(getActivity()));
		tvUnitSelVal = (TextView) findViewById(R.id.tvUnitSelVal);
		tvUnitSelVal.setTypeface(Utils.getBold(getActivity()));
		tvUnitSelVal.setText(converter.getSelectedUnit());
		tvUnitSelVal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showUnitSpinner(converter.getUnitList().toArray(new String[0]));
			}
		});

		TextView tvUnitValueKey = (TextView) findViewById(R.id.tvUnitValueKey);
		tvUnitValueKey.setTypeface(Utils.getBold(getActivity()));
		tvUnitValueVal = (TextView) findViewById(R.id.tvUnitValueVal);
		tvUnitValueVal.setTypeface(Utils.getBold(getActivity()));
		tvUnitValueVal.setText("" + converter.getSelectedValue());
		tvUnitValueVal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CalcFragment calc = new CalcFragment();
				calc.show(getSupportFragmentManager(), "");
			}
		});

		mAdapter = new ConverterForceAdapter(getActivity(),
				R.layout.unit_converter_item);
		lvUnitConverter = (DragSortListView) findViewById(R.id.lvUnitConverter);
		lvUnitConverter.setDropListener(onDrop);
		lvUnitConverter.setAdapter(mAdapter);

		mAdapter.addAll(converter.getFavUnit());

		ImageView imgFavUnit = (ImageView) findViewById(R.id.imgFavUnit);
		imgFavUnit.setOnClickListener(favClickListener);
	}

	View.OnClickListener menuClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			getSlidingMenu().showMenu(true);
		}
	};

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {

		@Override
		public void drop(int from, int to) {
			if (from != to) {
				try {
					MyUnit item = mAdapter.getItem(from);
					mAdapter.remove(item);
					mAdapter.insert(item, to);
					mAdapter.notifyDataSetChanged();
					Utils.saveUnitOrderForce(getActivity(), mAdapter.getAll());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	View.OnClickListener favClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			initAction(v);
		}
	};

	public void onValueSet(String value) {
		converter.setSelectedValue(Double.valueOf(value));
		tvUnitValueVal.setText("" + converter.getSelectedValue());
		mAdapter.addAll(converter.getFavUnit());
	}

	private void checkUnitAndRefresh() {
		Debug.e("", "Refreshing...");

		String selectedUnit = converter.getSelectedUnit();
		ArrayList<String> unitList = converter.getFavUnitName();

		if (!unitList.contains(selectedUnit)) {
			Utils.setPref(getActivity(), Constant.SELECTED_UNIT_FORCE,
					MyForceUnitConverter.NEWTON);
			tvUnitSelVal.setText(MyForceUnitConverter.NEWTON);
		}

		mAdapter.addAll(converter.getFavUnit());
	}

	private void showUnitSpinner(final String[] data) {

		final Dialog a = new Dialog(this);
		Window w = a.getWindow();
		a.requestWindowFeature(Window.FEATURE_NO_TITLE);
		a.setContentView(R.layout.spinner);
		w.setBackgroundDrawableResource(android.R.color.transparent);

		TextView lblselect = (TextView) w.findViewById(R.id.dialogtitle);
		lblselect.setTypeface(Utils.getBold(getActivity()));
		lblselect.setText(R.string.unit_selection);

		ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_item, data);
		ListView lv = (ListView) w.findViewById(R.id.lvSpinner);
		lv.setAdapter(Adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> Adapterview, View view,
					int position, long l) {

				a.dismiss();

				Utils.setPref(getActivity(), Constant.SELECTED_UNIT_FORCE,
						data[position]);
				mAdapter.addAll(converter.getFavUnit());
				tvUnitSelVal.setText(data[position]);

			}
		});

		a.show();
	}

	QuickAction quickAction;
	public static final int ID_ADD_UNIT = 1;
	public static final int ID_FAV_UNIT = 2;

	private void initAction(View v) {
		quickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);

		ActionItem addUnit = new ActionItem(ID_ADD_UNIT,
				getString(R.string.add_unit), null);
		ActionItem favUnit = new ActionItem(ID_FAV_UNIT,
				getString(R.string.fav_unit), null);

		// add action items into QuickAction
		quickAction.addActionItem(addUnit);
		quickAction.addActionItem(favUnit);

		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						ActionItem actionItem = quickAction.getActionItem(pos);

						if (actionItem.getActionId() == ID_ADD_UNIT) {

							Intent intent = new Intent(getActivity(),
									AddForceUnitActivity.class);
							Utils.startActivityForResult(getActivity(), intent,
									101);

						} else if (actionItem.getActionId() == ID_FAV_UNIT) {

							Intent intent = new Intent(getActivity(),
									FavUnitActivity.class);
							intent.putExtra("units", new Gson().toJson(
									converter.getUnitList(),
									new TypeToken<ArrayList<String>>() {
									}.getType()));

							intent.putExtra("unit_type", MenuFragment.MenuItem.FORCE);

							Utils.startActivityForResult(getActivity(), intent,
									101);

						}

					}

				});

		quickAction.show(v);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
			converter.refreshCustomUnit();
			checkUnitAndRefresh();
		}

	}

}
