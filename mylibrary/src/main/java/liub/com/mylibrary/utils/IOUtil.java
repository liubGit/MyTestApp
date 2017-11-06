package liub.com.mylibrary.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * io
 * Created by liub on 2017/10/26 .
 */
public class IOUtil {

    public static String getValues(InputStream inPut) {
        String value;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inPut, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            value = sb.toString();
        } catch (Exception e) {
            value = null;
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outputStream.toString();
    }
}
