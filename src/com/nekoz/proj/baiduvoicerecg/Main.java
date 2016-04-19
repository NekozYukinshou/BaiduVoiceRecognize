package com.nekoz.proj.baiduvoicerecg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("!main");
        if (args.length <= 0) {
            System.out.print("Usage: java -jar BaiduVoiceRecognize.jar VoxFile-16bit-Mono.wav");
            System.exit(0);
        }
        R.check();
        try {
            String result = regconize(args[0]);
            if (args.length < 2)
                System.out.print(result);
            else {
                File out = new File(args[1]);
                out.createNewFile();
                BufferedWriter bf = new BufferedWriter(new FileWriter(out));
                if (null != result) {
                    logger.info("Recognition: " + result);
                    bf.write(result);
                    bf.flush();
                    bf.close();
                } else
                    logger.warning("No recognition");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encode and send vox data to Baidu Vox Recognizer.
     * @param pathname  Path to raw WAV file
     * @return          n-best recognized result
     */
    private static String regconize(String pathname) {
        Logger logger = Logger.getLogger("!regconize");
        try {
            Vox vox = new Vox(pathname);
            JSONObject request = buildJSON("wav", "16000", "", "", "", vox.toBase64(), vox.length());
            logger.info("Built JSON: " + request.toString());
            RequestHandler handler = new RequestHandler();
            String response = handler.sendPostRequest(request, new URL(R.url));
            logger.info("Response: " + response);
            JSONObject resultJSON = new JSONObject(response);
            if (resultJSON.isNull("result"))
                logger.warning("Server returns error (" + resultJSON.get("err_no") + ": " + resultJSON.get("err_msg") + ")");
            else
                return new JSONObject(response).getJSONArray("result").getString(0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    return null;
    }

    /**
     * Build JSON from parameters.
     * @param format    语音压缩的格式，请填写上述格式之一，不区分大小写
     * @param rate      采样率，支持 8000 或者 16000
     *        channel 	声道数，仅支持单声道，请填写 1
     * @param cuid      用户唯一标识，用来区分用户，填写机器 MAC 地址或 IMEI 码，长度为60以内
     * @param token     开放平台获取到的开发者 access_token
     *        ptc       协议号，下行识别结果选择，默认 nbest 结果
     * @param lan       语种选择，中文=zh、粤语=ct、英文=en，不区分大小写，默认中文
     * @param speech    真实的语音数据 ，需要进行base64 编码
     * @param len       原始语音长度，单位字节
     * @return          Corresponding JSON in String
     */
    private static JSONObject buildJSON(
            String  format,
            String  rate,
            String  cuid,
            String  token,
            String  lan,
            String  speech,
            long    len
    ) {
        Logger logger = Logger.getLogger("!buildJSON");

        if ("".equals(cuid))
            cuid = R.cuid;
        if ("".equals(token))
            token = R.token;
        if ("".equals(lan))
            lan = "zh";
        try {
            JSONObject json = new JSONObject();
            json    .put("format",  format)
                    .put("rate",    rate)
                    .put("channel", 1)
                    .put("cuid",    cuid)
                    .put("token",   R.fetchToken())
                    .put("len",     len)
                    .put("speech",  speech)
                    .put("lan",     lan);
            return json;
        } catch (JSONException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    return null;
    }
}
