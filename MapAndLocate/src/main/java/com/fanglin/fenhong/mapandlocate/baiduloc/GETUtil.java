package com.fanglin.fenhong.mapandlocate.baiduloc;

import android.os.AsyncTask;
import android.util.Patterns;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by plucky on 15-8-16.
 */
public class GETUtil {
    public GETUtil() {

    }


    public GETCallBack mcb;

    public GETUtil setCallBack(GETCallBack cb) {
        this.mcb = cb;
        return this;
    }

    public void get(String url) {
        if (url == null || !Patterns.WEB_URL.matcher(url).matches()) {
            if (mcb != null) mcb.onEnd(false, "invalid url");
            return;
        }

        new task().execute(url);
    }


    public interface GETCallBack {
        public void onStart();

        public void onEnd(boolean isSuccess, String res);
    }

    public class task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mcb != null) mcb.onStart();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                if (mcb != null) mcb.onEnd(false, "inner error");
                return;
            }

            if (mcb != null)
                mcb.onEnd(true, s);
        }

        @Override
        protected void onCancelled() {

            if (mcb != null) mcb.onEnd(false, null);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient ht = new DefaultHttpClient();
                HttpGet hget = new HttpGet(params[0]);
                HttpResponse res = ht.execute(hget);
                if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] b = EntityUtils.toByteArray(res.getEntity());
                    return new String(b, "UTF-8");
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }
}
