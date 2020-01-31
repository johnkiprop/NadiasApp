package com.example.android.restful.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {

    /**
     * Returns text from a URL on a web server
     *
     * @return
     * @throws IOException
     */
    public static String downloadUrl(RequestPackage requestPackage) throws IOException {
            String address = requestPackage.getEndpoint();
            String encodedParams = requestPackage.getEncodedParams();
            if (requestPackage.getMethod().equals("GET")&&encodedParams.length()>0 ){
                address = String.format("%s?%s", address, encodedParams);
            }
            //Okhttp code
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(address);
        Request request = requestBuilder.build();

        if (requestPackage.getMethod().equals("POST")) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            Map<String, String> params = requestPackage.getParams();
            for (String key:params.keySet()) {
               builder.addFormDataPart(key, params.get(key));
            }
            RequestBody requestBody = builder.build();
            requestBuilder.method("POST", requestBody);

        }

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()){
            return response.body().string();
        }else {
            throw  new IOException("Exception response code: "+ response.code());
        }


            //below is lines of code without use of Okhttp
        /*
        InputStream is = null;
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestPackage.getMethod());
            conn.setDoInput(true);
            if (requestPackage.getMethod().equals("GET")&&encodedParams.length()>0 ){
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(requestPackage.getEncodedParams());
                writer.flush();
                writer.close();
            }

            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Got response code " + responseCode);
            }
            is = conn.getInputStream();
            return readStream(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
        */
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     *
     */
    /*
    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
*/
}
