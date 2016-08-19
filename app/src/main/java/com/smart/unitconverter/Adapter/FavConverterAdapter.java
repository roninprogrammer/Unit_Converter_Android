package com.smart.unitconverter.Adapter;

import java.util.ArrayList;


import com.smart.unitconverter.Home.FavUnitActivity;
import com.smart.unitconverter.Home.MenuFragment;
import com.smart.unitconverter.R;
import com.smart.unitconverter.Utils.Constant;
import com.smart.unitconverter.Utils.Debug;
import com.smart.unitconverter.Utils.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FavConverterAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<FavUnitActivity.FavUnit> data = new ArrayList<FavUnitActivity.FavUnit>();

	public FavConverterAdapter(Context c) {
		infalter = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = c;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public FavUnitActivity.FavUnit getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addAll(ArrayList<FavUnitActivity.FavUnit> files) {

		try {
			this.data.clear();
			this.data.addAll(files);

		} catch (Exception e) {
			Utils.sendExceptionReport(e);
		}

		notifyDataSetChanged();
	}

	public void changeSelection(int position) {
		data.get(position).isChecked = !data.get(position).isChecked;
		notifyDataSetChanged();
	}

	public void selectAll(boolean state) {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).isChecked = state;
		}

		notifyDataSetChanged();
	}

	public boolean isSelectedAll() {
		for (int i = 0; i < data.size(); i++) {
			if (!data.get(i).isChecked) {
				return false;
			}
		}

		return true;
	}

	public void saveBlackList(String type) {
		ArrayList<String> unitList = new ArrayList<String>();

		for (FavUnitActivity.FavUnit favUnit : data) {
			if (!favUnit.isChecked) {
				unitList.add(favUnit.name);
			}
		}

		Debug.e("", "BlackList # " + unitList.size());

		String unitStr = new Gson().toJson(unitList,
				new TypeToken<ArrayList<String>>() {
				}.getType());

		if (type.equalsIgnoreCase(MenuFragment.MenuItem.AREA)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_AREA, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.LENGTH)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_LENGTH, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.DIGITAL_STORAGE)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_STORAGE, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.FORCE)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_FORCE, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.MASS)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_MASS, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.PRESSURE)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_PRESSURE, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.SPEED)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_SPEED, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.TEMPERATURE)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_TEMPERATURE,
					unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.VOLUME)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_VOLUME, unitStr);
		} else if (type.equalsIgnoreCase(MenuFragment.MenuItem.TIME)) {
			Utils.setPref(mContext, Constant.FAVOURITE_UNIT_TIME, unitStr);
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {

			convertView = infalter.inflate(R.layout.unit_fav_item, null);
			holder = new ViewHolder();

			holder.tvUnitName = (TextView) convertView
					.findViewById(R.id.tvUnitName);
			holder.tvUnitName.setTypeface(Utils.getNormal(mContext));

			holder.chkUnitFav = (ImageView) convertView
					.findViewById(R.id.chkUnitFav);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {

			holder.tvUnitName.setText("" + data.get(position).name);
			holder.chkUnitFav.setSelected(data.get(position).isChecked);

		} catch (Exception e) {
			Utils.sendExceptionReport(e);
		}

		return convertView;
	}

	public class ViewHolder {
		TextView tvUnitName;
		ImageView chkUnitFav;
	}

}
