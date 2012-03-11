package com.hbut.alvin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hbut.util.PersonSbj;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends ListActivity {


	final static int SERVICENOTDONE = 1;
	final static int NOTSBJERROR = 2;
	public static final int MENU_ABOUT = Menu.FIRST+1;
	ImageButton outBtn;
	ImageButton updateBtn;
	TextView infText;
	HbutApp myapp;
	ListView showListView;
	List<PersonSbj> gradesContent;
	private List<Map<String, Object>> listData;
	String[] items = { "sbjName", "sbjNote", "pGrade", "grdlevel" };
	int[] itemsID = { R.id.sbjName, R.id.sbjNote, R.id.pGrade, R.id.grdlevel };
	Handler handler;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE,MENU_ABOUT,Menu.NONE,"关于")
		.setIcon(R.drawable.about_ic);
		 
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_ABOUT:
			Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case SERVICENOTDONE:
					Toast.makeText(ShowActivity.this, "数据还在处理……",
							Toast.LENGTH_SHORT).show();
					break;
				case NOTSBJERROR:
					Toast.makeText(ShowActivity.this, "只有你选了这门课哦！",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}

		};
		outBtn = (ImageButton) findViewById(R.id.outBtn);
		updateBtn = (ImageButton) findViewById(R.id.updateBtn);
		infText = (TextView) findViewById(R.id.inf);
		showListView = (ListView) findViewById(android.R.id.list);
		myapp = (HbutApp) getApplicationContext();
		gradesContent = myapp.getpSbjList();
		infText.setText(myapp.getPsi().getName() + "~"
				+ myapp.getPsi().getCls());

		listData = buildListData();
		showListView.setAdapter(new RowAdapter(this, listData, R.layout.prow,
				items, itemsID));

		showListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (!myapp.isClsDownloadEnd()) {
					handler.sendMessage(handler.obtainMessage(SERVICENOTDONE));
					return;
				}
				Map<String, Object> map = (Map<String, Object>) arg0
						.getItemAtPosition(arg2);
				String sbjID = ((String) map.get("sbjNote")).split("_")[0];
				if (myapp.getClsSbj().get(sbjID) == null) {
					handler.sendMessage(handler.obtainMessage(NOTSBJERROR));
					return;
				}
				String sbjName = (String) map.get("sbjName");
				Intent intent = new Intent(ShowActivity.this,
						ClassShowActivity.class);
				intent.putExtra("sbjID", sbjID);
				intent.putExtra("sbjName", sbjName);
				startActivity(intent);

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		List<Map<String, Object>> oldListData = listData;
		listData = buildListData();
		if(oldListData.equals(listData))
			return;
		showListView.setAdapter(new RowAdapter(this, listData, R.layout.prow,
				items, itemsID));
	}

	public void onUpdateClicked(View updateBtn) {
		Intent intent = new Intent(ShowActivity.this, UpdateActivity.class);
		startActivity(intent);
	}
	public void onOutClicked(View updateBtn){
		finish();
	}
	private List<Map<String, Object>> buildListData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (PersonSbj n : gradesContent) {
			map = new HashMap<String, Object>();
			map.put("sbjName", n.getSbjName());
			map.put("sbjNote", n.getSbjID() + "_" + n.getSbjNote());
			// if (n.getSbjLevel() == null) {
			map.put("pGrade", Integer.toString(n.getSbjGrade()));
			int sbjGrade = n.getSbjGrade();
			if (sbjGrade >= 90)
				map.put("grdlevel", R.drawable.grade5_ic);
			else if(sbjGrade < 90 && sbjGrade>=80)
				map.put("grdlevel", R.drawable.grade4_ic);
			else if(sbjGrade < 80 && sbjGrade>=70)
				map.put("grdlevel", R.drawable.grade3_ic);
			else if(sbjGrade < 70 && sbjGrade>=60)
				map.put("grdlevel", R.drawable.grade2_ic);
			else if(sbjGrade < 60)
				map.put("grdlevel", R.drawable.grade1_ic);
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
