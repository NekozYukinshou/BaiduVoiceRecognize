package com.nekoz.proj.baiduvoicerecg;

/**
 * Title      : R.java
 * Description:
 * Copyright  : BY-NC-SA 3.0
 *
 * @author Nekoz Yukinshou
 * @version 1.0
 */

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

final class R {
    static String
            appId = "",
            apiKey = "",
            secretKey = "",
            cuid = "RecognizerTester",
            url = "http://vop.baidu.com/server_api";
    static String token = null;


    static void check() {
        if ("".equals(appId)) {
            System.out.print("App ID: ");
            Scanner sc = new Scanner(System.in);
            appId = sc.next();
        }
        if ("".equals(apiKey)) {
            System.out.print("API Key: ");
            Scanner sc = new Scanner(System.in);
            apiKey = sc.next();
        }
        if ("".equals(secretKey)) {
            System.out.print("Secret Key: ");
            Scanner sc = new Scanner(System.in);
            secretKey = sc.next();
        }
    }

    /**
     * Fetch token from server.
     * @param apiKey    The API Key of this app
     * @param secretKey The Secret Key of this app
     * @return          Token received from the server
     */
    static String fetchToken(String apiKey, String secretKey) {
        final String server = "https://openapi.baidu.com/oauth/2.0/token";

        Logger logger = Logger.getLogger("!fetchToken");
        try {
            URL url = new URL(server);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            // Set properties
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // Construct POST parameters
            String para = "grant_type=client_credentials&" +
                    "&client_id="        +  apiKey +
                    "&client_secret="    +  secretKey;
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(para);
            out.flush();
            out.close();
            // Get response
            // int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer("");
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject responseJSON = new JSONObject(response.toString());
            logger.info("Received JSON: " + responseJSON.toString());
            // Get the token
            token = responseJSON.get("access_token").toString();
            logger.info("Token: " + token);
            return token;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String fetchToken() {
        return fetchToken(apiKey, secretKey);
    }
}
