package com.hbut.alvin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hbut.util.ClsStuSbj;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ClassShowActivity extends ListActivity {



	final static int NOTSBJERROR = 1;
	
	ImageButton backBtn ;
	TextView clsText;
	ListView clsListView;
	HbutApp myapp;
	private List<Map<String, Object>> cListData;
	Map<String,List<ClsStuSbj>> clsStuSbjMap;//cls sbj map in application 
	List<ClsStuSbj> stuSbjList;//the chosen stu grade of paticular sbj
	String[] items = {"sbjRank","stuName","stuGrade"};
	int[] itemsID = {R.id.sbjRank,R.id.stuName,R.id.stuGrade};
	String sbjName = null;
	String sbjID = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cshow);

		//get the sbjID and sbjName
		Intent intent = getIntent();
		sbjID = intent.getStringExtra("sbjID");
		sbjName = intent.getStringExtra("sbjName");
		//initialize
		myapp = (HbutApp)getApplicationContext();
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		clsText = (TextView) findViewById(R.id.clsText);
		clsListView = (ListView) findViewById(android.R.id.list);
		clsText.setText(sbjName);
		//get data from app
		clsStuSbjMap = myapp.getClsSbj();
		stuSbjList = clsStuSbjMap.get(sbjID);
		if(stuSbjList == null){
			finish();
			return;
		}
		//setAdapter
		cListData =buildCListData();
		clsListView.setAdapter(new RowAdapter(this, cListData, R.layout.cprow, items, itemsID));
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myapp = (HbutApp)getApplicationContext();
		clsStuSbjMap = myapp.getClsSbj();
		stuSbjList = clsStuSbjMap.get(sbjID);
		if(stuSbjList == null){
			finish();
			return;
		}
	}

	public void onBackCliked(View backBtn){
		finish();
	}
	
	
	private List<Map<String,Object>> buildCListData(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String,Object> map = null;
		int count = 0;
		for(ClsStuSbj n :stuSbjList){
			map = new HashMap<String, Object>();
			map.put("stuName", n.getStuName());
			map.put("stuGrade",Integer.toString(n.getStuGrade()));
			if(count == 0){
				map.put("sbjRank", R.drawable.rank1_ic);
				count ++;
			}else if(count == 1){
				map.put("sbjRank", R.drawable.rank2_ic);
				count ++;
			}else if (count == 2){
				map.put("sbjRank", R.drawable.rank3_ic);
				count ++;
			}else{
				map.put("sbjRank", R.drawable.rankother_ic);
			}
			
			list.add(map);
		}
		return list;
		
	}
	
	class RowAdapter extends SimpleAdapter{

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View row = convertView;
			if(row == null){
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.cprow, parent, false);
			}
			CViewHolder holder = (CViewHolder) row.getTag();
			if(holder == null){
				holder = new CViewHolder(row);
				row.setTag(holder);
			}
			holder.sbjRank.setImageResource((Integer) cListData.get(position).get("sbjRank"));
			holder.stuName.setText((String) cListData.get(position).get("stuName"));
			holder.stuGrade.setText((String)cListData.get(position).get("stuGrade"));
			
			return row;
		}

		public final class CViewHolder{
			ImageView sbjRank = null;
			TextView stuName = null;
			TextView stuGrade = null;
			private CViewHolder(View base) {
				this.sbjRank = (ImageView) base.findViewById(R.id.sbjRank);
				this.stuName = (TextView) base.findViewById(R.id.stuName);
				this.stuGrade = (TextView) base.findViewById(R.id.stuGrade);
			}
			
		}
		public RowAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}
		
	}
	
}
