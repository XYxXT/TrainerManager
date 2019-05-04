package tfg.trainermanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tfg.trainermanager.model.DB_Key;
import tfg.trainermanager.model.DB_Url;
import tfg.trainermanager.model.HttpJsonParser;

public class MainActivity extends AppCompatActivity {

    private EditText userName_T, userPass_T;
    private Button login_B;
    private TextView idUser_L;
    private ProgressDialog pDialog;

    private Map<String, String> requestParams, resultParams;
    private int requestResult = HttpJsonParser.REQUEST_RESULT_NOT_SUCCESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setController();
    }

    private void setController(){
        userName_T = findViewById(R.id.userName_T);
        userPass_T = findViewById(R.id.userPass_T);
        login_B = findViewById(R.id.login_B);
        idUser_L = findViewById(R.id.idUser_L);
    }


    public void onclick_login_B(View view){
        new Login_AsyncTask().execute();
    }


    private class Login_AsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Connecting. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            requestParams = new HashMap<>();
            requestParams.put("username", userName_T.getText().toString());
            requestParams.put("userpass", userPass_T.getText().toString());
            requestParams.put("login_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    DB_Url.URL_LOGIN , requestParams);
            try {
                requestResult = jsonObject.getInt(HttpJsonParser.REQUEST_KEY_SUCCESS);

                if (requestResult == HttpJsonParser.REQUEST_RESULT_SUCCESS) {
                    resultParams = new HashMap<>();
                    JSONObject data = jsonObject.getJSONObject(HttpJsonParser.REQUEST_KEY_DATA);
                    resultParams.put(DB_Key.id_user, data.getString(DB_Key.id_user));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (requestResult == HttpJsonParser.REQUEST_RESULT_SUCCESS) {
                        idUser_L.setText(resultParams.get(DB_Key.id_user));
                    }
                }
            });
        }

    }

}
