package tfg.trainermanager.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class HttpJsonParser {

    public static final String REQUEST_KEY_SUCCESS = "success";
    public static final String REQUEST_KEY_DATA = "data";

    public static final int REQUEST_RESULT_SUCCESS = 1;
    public static final int REQUEST_RESULT_NOT_SUCCESS = 0;

    private static JSONObject jObj = null;

    // function get json from url
    // by making HTTP POST
    public JSONObject makeHttpRequest(String url, Map<String, String> params) {

        try {
            Uri.Builder builder = new Uri.Builder();
            URL urlObj;
            String encodedParams = "";
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            if (builder.build().getEncodedQuery() != null) {
                encodedParams =  builder.build().getEncodedQuery();

            }

            urlObj = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(encodedParams.getBytes().length));
            urlConnection.getOutputStream().write(encodedParams.getBytes());

            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            String json = sb.toString();

            jObj = new JSONObject(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (Exception e) {
            Log.e("Exception", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}