package tools;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author 10626
 */
public class ReadAsChars {
    public static String read(HttpServletRequest request)
{

    BufferedReader br = null;
    StringBuilder sb = new StringBuilder("");
    try
    {
        br = request.getReader();
        String str;
        while ((str = br.readLine()) != null)
        {
            sb.append(str);
        }
        br.close();
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
    finally
    {
        if (null != br)
        {
            try
            {
                br.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    return sb.toString();
}
}
