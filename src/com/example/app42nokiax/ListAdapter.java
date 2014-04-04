package com.example.app42nokiax;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shephertz.app42.paas.sdk.android.storage.Storage.JSONDocument;

public class ListAdapter extends BaseAdapter {

	private Activity activity;
	ArrayList<JSONDocument> jsonDoc;
	private static LayoutInflater inflater = null;

	public ListAdapter(Activity a, ArrayList<JSONDocument> jsonDocumet) {
		activity = a;
		jsonDoc = jsonDocumet;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return jsonDoc.size();
	}

	

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView name;
		public TextView index;
		public TextView income;

		// public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
	
		if (convertView == null) {
			vi = inflater.inflate(
					R.layout.list_item, null);
		
			holder = new ViewHolder();
			holder.name = (TextView) vi.findViewById(R.id.name);
			holder.index = (TextView) vi.findViewById(R.id.index);
			holder.income = (TextView) vi.findViewById(R.id.income);
		
			vi.setTag(holder);

		} else

			holder = (ViewHolder) vi.getTag();
		JSONObject data=null;
		try {
			 data=new JSONObject(jsonDoc.get(position).jsonDoc);
			     holder.name.setText(data.getString("userName"));
				holder.index.setText(""+(++position));
				holder.income.setText(""+data.get("income"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return vi;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}