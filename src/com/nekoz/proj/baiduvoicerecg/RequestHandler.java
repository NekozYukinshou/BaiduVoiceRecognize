package com.nekoz.proj.baiduvoicerecg;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Title      : RequestHandler.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Nekoz Yukinshou
 * @version 1.0
 */

class RequestHandler {
    /**
     * Send post request to designated url.
     *
     * @param data POST request contents
     * @param url  Target URL
     * @return Response string
     */
    String sendPostRequest(JSONObject data, URL url) {
        try {
            // Set properties
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Content-length", String.valueOf(data.length()));
            conn.setRequestProperty("Accept", "application/json");

            // POST request
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(data.toString());
            out.flush();
            out.close();

            // Get the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
}
