package com.example.app42nokiax;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;

public class LoginActivity extends Activity {
	private ProgressDialog progressDialog;
	private EditText userName;
	private EditText password;
	private EditText emailid;
	private TextView errorMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		progressDialog = new ProgressDialog(this);
		userName = (EditText) this.findViewById(R.id.uname);
		password = (EditText) this.findViewById(R.id.pswd);
		emailid = (EditText) this.findViewById(R.id.email);
		errorMsg = (TextView) this.findViewById(R.id.error_msg);
		intitialize();
	}

	private void intitialize() {
		App42API.initialize(
				this,
				"<Your App42 API Key>",
				"<Your App42 Secret Key>");
	}

	public void onSigninClicked(View view) {
		errorMsg.setVisibility(View.GONE);
		progressDialog.setMessage("signing in..");
		progressDialog.show();
		authenticateUser(userName.getText().toString(), password.getText()
				.toString());
	}

	private void onUserAuthenticated() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText("User Successfully Authenticated with APP42");
				goToHome();
			}
		});
	}

	private void goToHome() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("userName", userName.getText().toString());
		startActivity(intent);
	}

	private void onUserRegistered() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText("User Successfully Registered with APP42");
				goToHome();
			}
		});
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

	public void onRegisterClicked(View view) {
		errorMsg.setVisibility(View.GONE);
		progressDialog.setMessage("registering user..");
		progressDialog.show();
		registerUser(userName.getText().toString(), password.getText()
				.toString(), emailid.getText().toString());
	}

	private void registerUser(final String user, final String pass,
			final String email) {

		App42API.buildUserService().createUser(user, pass, email,
				new App42CallBack() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						onUserRegistered();

					}

					@Override
					public void onException(Exception arg0) {
						// TODO Auto-generated method stub
						onApp42Exception(arg0);
					}
				});
	}

	private void authenticateUser(final String user, final String pass) {

		App42API.buildUserService().authenticate(user, pass,
				new App42CallBack() {

					@Override
					public void onSuccess(Object arg0) {
						// TODO Auto-generated method stub
						onUserAuthenticated();

					}

					@Override
					public void onException(Exception arg0) {
						// TODO Auto-generated method stub
						onApp42Exception(arg0);

					}
				});

	}

}
