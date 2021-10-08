package tools;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;

/**
 * @author 10626
 */
public abstract class record {
    public static void insert(String ip, String stat, String referer,String path) throws ClassNotFoundException, SQLException, IOException {
        String JDBC_DRIVER="com.mysql.jdbc.Driver";
        String DB_URL="jdbc:mysql://localhost/tunion";

        // 数据库的用户名与密码，需要根据自己的设置
        String USER="tunion";
        String PASS="Sheng75050722";
        String locate = GetLocate.getLocate(ip);
        Class.forName(JDBC_DRIVER);
        Connection connect = DriverManager.getConnection(DB_URL,USER,PASS);

        Statement stmt = connect.createStatement();
        String sql = "INSERT INTO `userrecord` (`IP`, `Time`,`Stat`, `Path`, `referer`, `location`) VALUES (?, ?, ?,?,?,?)";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1,ip);
        ps.setTimestamp(2,new Timestamp(Calendar.getInstance().getTimeInMillis()));
        ps.setString(3,stat);
        ps.setString(4,path);
        ps.setString(5,referer);
        ps.setString(6,locate);
        ps.executeUpdate();
        // 完成后关闭
        ps.close();
        stmt.close();
        connect.close();
    }
}
