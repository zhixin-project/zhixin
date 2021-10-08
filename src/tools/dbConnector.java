package tools;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 使用 db.properties 文件配置数据库连接属性，<br />
 * 并实现对MySQL数据库的连接
 *
 * @author pan_junbiao
 *
 */
public class dbConnector
{
    private static String DRIVER_CLASS; // 数据库驱动
    private static String DB_URL; // 数据库连接地址
    private static String DB_USER; // 数据库用户名称
    private static String DB_PASSWORD; // 数据库用户密码

    public static Connection getConnection()
    {
        try
        {
            // 创建Properties类对象
            Properties properties = new Properties();

            // 读取properties属性文件到输入流中
            InputStream is = dbConnector.class.getResourceAsStream("/db.properties");

            // 从输入流中加载属性列表
            properties.load(is);

            // 获取数据库连接属性值
            DRIVER_CLASS = properties.getProperty("DRIVER_CLASS");
            DB_URL = properties.getProperty("DB_URL");
            DB_USER = properties.getProperty("DB_USER");
            DB_PASSWORD = properties.getProperty("DB_PASSWORD");

            // 加载数据库驱动类
            Class.forName(DRIVER_CLASS);
            Connection conn =null;
            // 获取数据库连接对象
            return  conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        } catch (ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}