package com.example.app42nokiax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.Storage.JSONDocument;

public class MainActivity extends Activity {
	private ProgressDialog progressDialog;
	private TextView errorMsg;
	private final String DbName = "NokiaX";
	private final String CollName = "userIncome";
	private String userName;
	private ListView list;
	private EditText income;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressDialog = new ProgressDialog(this);
		errorMsg = (TextView) this.findViewById(R.id.error_msg);
		userName = getIntent().getStringExtra("userName");
		list = (ListView) this.findViewById(R.id.users_list);
		income = (EditText) this.findViewById(R.id.income);
	}

	public void onGetStorageClicked(View view) {
		errorMsg.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		progressDialog.setMessage("Retriving Documents..");
		progressDialog.show();
		getStorage();
	}

	public void onInsertDocClicked(View view) {
		errorMsg.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		progressDialog.setMessage("Inserting Document..");
		progressDialog.show();
		insertDoc(getJsonDoc());
	}

	private JSONObject getJsonDoc() {
		JSONObject json = new JSONObject();
		try {
			json.put("userName", userName);
			json.put("income", Integer.parseInt(income.getText().toString()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	private void onApp42Exception(final Exception ex) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(ex.toString());
			}
		});

	}

	private void showResult(final Storage storageDoc) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
				list.setVisibility(View.VISIBLE);

				list.setAdapter(new ListAdapter(MainActivity.this, storageDoc
						.getJsonDocList()));

			}
		});
	}

	private void insertResult(final Storage storageDoc) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(storageDoc.toString());

			}
		});
	}

	private void insertDoc(final JSONObject jsonDoc) {

		App42API.buildStorageService().insertJSONDocument(DbName, CollName,
				jsonDoc, new App42CallBack() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						insertResult((Storage) arg0);
					}

					@Override
					public void onException(Exception arg0) {
						// TODO Auto-generated method stub
						onApp42Exception(arg0);

					}
				});

	}

	private void getStorage() {
		HashMap<String, String> otherMetaHeaders = new HashMap<String, String>();
		otherMetaHeaders.put("orderByDescending", "income");
		App42API.buildStorageService().setOtherMetaHeaders(otherMetaHeaders);
		App42API.buildStorageService().findAllDocuments(DbName, CollName,
				new App42CallBack() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						showResult((Storage) arg0);
					}

					@Override
					public void onException(Exception arg0) {
						// TODO Auto-generated method stub
						onApp42Exception(arg0);
					}
				});
	}

}
