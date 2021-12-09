package tools;

import com.alibaba.fastjson.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author 10626
 */
public class GetLocate {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * 创建链接
     *
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = JSONObject.parseObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static String getLocate(String ip) throws JSONException, IndexOutOfBoundsException {
        JSONObject json=new JSONObject();
        try{
            json = readJsonFromUrl("http://ip-api.com/json/"+ip+"?lang=zh-CN");
        }
        catch (IOException e){
            e.printStackTrace();
        }
//        System.out.println(json.toString());
        String[] str=new String[6];
        try{
            str[0] = (String)json.get("country");
            str[1] = (String)json.get("regionName");
            str[2] = (String)json.get("city");
            str[3] = (String)json.get("isp");
            str[4] = (String)json.get("org");
            str[5] = (String)json.get("as");
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        StringBuilder locate= new StringBuilder();
        for(String i:str){
            if(i!=null){
                locate.append(i);
            }
        }
        return locate.toString();
    }
}
