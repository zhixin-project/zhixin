package tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 将数据集中的数据转成Json数据格式
 * @author 10626
 */
public class rsToJSON {


    /**
     * 将数据集中的数据转成Json数据格式
     *  获取数据位Json格式
     * @param resultSet 查询出来的数据集
     * @return JSON 转换后的对象
     */
    public static JSON resultSetToJSON(ResultSet resultSet) {
//        数据集JSON格式
//        数据库中每行的数据对象
        JSONObject rowObj = null;
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        int count=0;
        try {
//          ResultSetMetaData 有关 ResultSet 中列的名称和类型的信息。
            ResultSetMetaData rsmd = resultSet.getMetaData();
//        遍历数据集，将数据集读取到data中
            while (resultSet.next()) {
                count++;
//          每读取一行，新建对象
                rowObj = new JSONObject();
//              获取表列数
                int columnCount = rsmd.getColumnCount();
//            列从1开始，要等于
                for (int i = 1; i <= columnCount; i++) {
//                获取列名
                    String columnName = rsmd.getColumnName(i);
//                取数据
                    String value = resultSet.getString(columnName);
//                添加到rowObj中
                    rowObj.put(columnName, value);
                }
//                添加到数据集Json
                data.add(rowObj);
            }
            // 将数据的条数保存到result中
            result.put("count",count);
            result.put("data",data);
            if(count!=0){
                result.put("code","0");
                result.put("msg","获取成功，祝您生活愉快！");
            }else {
                result.put("code","400");
                result.put("msg","暂无数据！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param resultSet  对象
     * @param page  页码
     * @param limit  每页限制的数据数量
     * @return  Json转换后的对象，（包括数据）
     */
    public static JSON resultSetToJSON(ResultSet resultSet, int page, int limit) {
//        数据集JSON格式
//        数据库中每行的数据对象
        JSONObject rowObj = null;
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        int count=0;
        try {
//          ResultSetMetaData 有关 ResultSet 中列的名称和类型的信息。
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //  进行分页
            resultSet.absolute(limit*(page-1));
            int end = limit*page;
//        遍历数据集
            while (resultSet.next()&&resultSet.getRow()<=end) {
//          每读取一行，新建对象
                rowObj = new JSONObject();
//              获取表列数
                int columnCount = rsmd.getColumnCount();
//            列从1开始，要等于
                for (int i = 1; i <= columnCount; i++) {
//                获取列名
                    String columnName = rsmd.getColumnName(i);
//                取数据
                    String value = resultSet.getString(columnName);
//                添加到rowObj中
                    rowObj.put(columnName, value);
                }
//                添加到数据集Json
                data.add(rowObj);
            }
            resultSet.last();
            // 获取数据的总行数
            count=resultSet.getRow();
            result.put("count",count);
            result.put("data",data);
            if(count!=0){
                result.put("code","0");
                result.put("msg","获取成功，祝您生活愉快！");
            }else {
                result.put("code","500");
                result.put("msg","暂无数据！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @return  拿到错误报错
     */
    public static JSON getError() {
//        数据集JSON格式
//        数据库中每行的数据对象
        JSONObject rowObj = null;
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        result.put("code","400");
        result.put("msg","读取数据时发生错误！");
        int count=0;
        result.put("count",count);
        result.put("data",data);
        return result;
    }

    /**
     * 自己编写的报错json格式
     * @return  当用户没有登录时就会报错Json格式数据
     */
    public static JSON getErrorNoLogin() {
//        数据集JSON格式
//        数据库中每行的数据对象
        JSONObject rowObj = null;
        JSONObject result = new JSONObject();
        JSONArray data = new JSONArray();
        result.put("code","403");
        result.put("msg","您还没有登录管理系统！");
        int count=0;
        result.put("count",count);
        result.put("data",data);
        return result;
    }
}

