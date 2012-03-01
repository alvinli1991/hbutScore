package com.hbut.alvin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hbut.util.PersonSbj;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShowActivity extends ListActivity {

	ImageButton configBtn;
	ImageButton updateBtn;
	TextView infText;
	HbutApp myapp;
	ListView showListView;
	List<PersonSbj> gradesContent;
	private List<Map<String, Object>> listData;
	String[] items = { "sbjName", "sbjNote", "pGrade", "grdlevel" };
	int[] itemsID = { R.id.sbjName, R.id.sbjNote, R.id.pGrade, R.id.grdlevel };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		configBtn = (ImageButton) findViewById(R.id.configBtn);
		updateBtn = (ImageButton) findViewById(R.id.updateBtn);
		infText = (TextView) findViewById(R.id.inf);
		showListView = (ListView) findViewById(android.R.id.list);
		myapp = (HbutApp) getApplicationContext();
		gradesContent = myapp.getpSbjList();
		listData = buildListData();
		infText.setText(myapp.getPsi().getName() + "~" + myapp.getPsi().getCls());
		showListView.setAdapter(new RowAdapter(this, listData, R.layout.prow,
				items, itemsID));
	}

	private List<Map<String, Object>> buildListData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (PersonSbj n : gradesContent) {
			map = new HashMap<String, Object>();
			map.put("sbjName", n.getSbjName());
			map.put("sbjNote", n.getSbjID() + "  " + n.getSbjNote());
			// if (n.getSbjLevel() == null) {
			map.put("pGrade", Integer.toString(n.getSbjGrade()));
			if (n.getSbjGrade() >= 90)
				map.put("grdlevel", R.drawable.head);
			else
				map.put("grdlevel", R.drawable.android);
			list.add(map);
		}

		return list;
	}

	class RowAdapter extends SimpleAdapter {

		// int ScreenWidth;
		// private int getScreenWidth()
		// {
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// return dm.widthPixels;
		// }
		// private int getPicWidth(int id){
		// Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
		// return bmp.getWidth();
		// }
		// private int getStringWidth(String target){
		// Paint p = new Paint();
		// return Math.round(p.measureText(target));
		// }
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.prow, parent, false);
			}

			PViewHolder holder = (PViewHolder) row.getTag();
			if (holder == null) {
				holder = new PViewHolder(row);
				row.setTag(holder);
			}

			// int picWidth = getPicWidth((Integer)
			// listData.get(position).get("grdlevel"));
			// int strWidth1 =
			// getStringWidth((String)listData.get(position).get("pGrade"));
			// int strWidth2 =
			// getStringWidth((String)listData.get(position).get("sbjName"));
			holder.pGrade
					.setText((String) listData.get(position).get("pGrade"));
			holder.grdlevel.setImageResource((Integer) listData.get(position)
					.get("grdlevel"));
			holder.sbjName.setText((String) listData.get(position).get(
					"sbjName"));

			holder.sbjNote.setText((String) listData.get(position).get(
					"sbjNote"));

			return row;
		}

		public final class PViewHolder {
			ImageView grdlevel = null;
			TextView pGrade = null;
			TextView sbjName = null;
			TextView sbjNote = null;

			public PViewHolder(View base) {
				this.grdlevel = (ImageView) base.findViewById(R.id.grdlevel);
				this.pGrade = (TextView) base.findViewById(R.id.pGrade);
				this.sbjName = (TextView) base.findViewById(R.id.sbjName);
				this.sbjNote = (TextView) base.findViewById(R.id.sbjNote);
			}
		}

		public RowAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			// ScreenWidth = getScreenWidth();
			// TODO Auto-generated constructor stub
		}

	}

}
