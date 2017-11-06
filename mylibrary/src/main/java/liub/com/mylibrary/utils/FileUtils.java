package liub.com.mylibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件工具
 * Created by liub on 2017/10/26 .
 */
public class FileUtils {
    public static boolean printLog = false;

    //APK下载目录
    public final static String APP_LOCAL_APK_PUBLIC_DIR_PATH = "/mytestapp/version/download/";


    enum FileType {
        IMAGE, VIDEO, FILE
    }


    /**
     * 输出记录到本地（txt格式）
     */
    public static void printLogToLocal(String content) {
        if (!Lmsg.isDebug || !printLog) return;
        try {
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            File logPath = getAppLogPath();
            if (logPath == null) return;
            String fileName = logPath.getCanonicalPath() + "/log_" + localSimpleDateFormat.format(new Date()) + ".txt";
            Lmsg.e("printLogToLocal日志路径：" + fileName);
            printTxtToLocal(fileName, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有日志文件
     */
    public static void clearAllLogFiles() {
        try {
            File file = getAppLogPath();
            if (file == null) {
                return;
            }
            deleteDir(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteDir(File dir) throws Exception {
        if (dir == null) {
            return;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            String[] var3 = children;
            int var4 = children.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String child = var3[var5];
                deleteDir(new File(dir, child));
            }
        } else {
            dir.delete();
        }
    }

    /**
     * 输出错误日志到本地（txt格式）
     */
    public static void printErrorLogToLocal(String content) {
        if (!Lmsg.isDebug || !printLog) return;
        try {
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            File path = getAppErrorLogPath();
            if (path == null) return;
            String fileName = path.getCanonicalPath() + "/log_" + localSimpleDateFormat.format(new Date()) + ".txt";
            Lmsg.e("printErrorLogToLocal日志路径：" + fileName);
            printTxtToLocal(fileName, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出错误日志到本地（txt格式）
     */
    public static void printLiveErrorLogToLocal(String content) {
        if (!Lmsg.isDebug || !printLog) return;
        try {
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            File path = getLiveErrorLogPath();
            if (path == null) return;
            String fileName = path.getCanonicalPath() + "/log_" + localSimpleDateFormat.format(new Date()) + ".txt";
            Lmsg.e("printErrorLogToLocal日志路径：" + fileName);
            printTxtToLocal(fileName, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出Txt到本地
     *
     * @param fileName 文件名（包括目录）
     * @param content  输出txt的内容
     */
    public static void printTxtToLocal(String fileName, String content) {
        try {
            File file = new File(fileName);

            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }

            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.flush();
            bw.write(localSimpleDateFormat.format(new Date()) + "-->>\r\n\r\n");
            bw.write(content);
            bw.write("\r\n\r\n\r\n\r\n\r\n\r\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片到本地
     *
     * @param bitmap         图片bitmap
     * @param compressFormat 图片compressFormat Bitmap.CompressFormat.JPEG或者Bitmap.CompressFormat.PNG
     * @param isCache        是否放到缓存目录（缓存目录可以随着app清除，没特殊存在意义的建议true）
     * @param context        如放到缓存目录，不可空
     */
    public static File saveImageToLocal(Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean isCache, Context context) throws Exception {
        String fileExtensions;
        if (compressFormat == Bitmap.CompressFormat.PNG)
            fileExtensions = "png";
        else
            fileExtensions = "jpg";
        File picFile = createNewFile(FileType.IMAGE, fileExtensions, isCache, context);
        if (picFile == null)
            throw new Exception("图片文件创建失败");

        FileOutputStream fos = new FileOutputStream(picFile);
        bitmap.compress(compressFormat, 100, fos);

        fos.flush();
        fos.close();
        return picFile;
    }

    /**
     * 内存卡是否存在
     */
    public static boolean sdCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取内存卡路径
     *
     * @return 内存卡路径file 内存卡不存在会返回null
     */
    public static File getSdCardPath() {
        if (sdCardExists())
            return Environment.getExternalStorageDirectory();
        return null;
    }

    /**
     * 获取app本地存放资源（图片、视频、文件）目录路径
     *
     * @return 目录路径file 内存卡不存在和文件夹创建错误会返回null
     */
    public static File getAppLocalFilePath(FileType fileType) {
        File fileParentPath = getAppLocalFileParentPath();
        if (fileParentPath == null)
            return null;

        File filePath = new File(fileParentPath, getFileTypeName(fileType) + "/");

        boolean isSuccess = true;
        if (!filePath.exists())
            isSuccess = filePath.mkdirs();
        return isSuccess ? filePath : null;
    }

    /**
     * 获取app日志存放目录路径
     *
     * @return 目录路径file 内存卡不存在和文件夹创建错误会返回null
     */
    public static File getAppLogPath() {
        return getFile(null, false, "/healthmall/log/");
    }

    /**
     * 获取app错误日志存放目录路径
     *
     * @return 目录路径file 内存卡不存在和文件夹创建错误会返回null
     */
    public static File getAppErrorLogPath() {
        return getFile(null, false, "/healthmall/errorLog/");
    }

    /**
     * 获取app直播错误日志存放目录路径
     *
     * @return 目录路径file 内存卡不存在和文件夹创建错误会返回null
     */
    public static File getLiveErrorLogPath() {
        return getFile(null, false, "/healthmall/liveLog/");
    }

    /**
     * 获取app本地存放缓存资源（图片、视频、文件）目录路径
     *
     * @return 文件路径file 内存卡不存在和文件夹创建错误会返回null
     */
    public static File getAppLocalCacheFilePath(Context context, FileType fileType) {
        return getFile(context, true, getFileTypeName(fileType) + "/");
    }

    /**
     * 获取app本地存放安装包目录路径
     *
     * @return 目录路径file 内存卡不存在和文件夹创建错误会返回null
     */
    public static File getAppLocalApkPath() {
        File sdCardPath = getSdCardPath();
        if (sdCardPath == null)
            return null;
        File filePath = new File(sdCardPath, APP_LOCAL_APK_PUBLIC_DIR_PATH);

        boolean isSuccess = true;
        if (!filePath.exists())
            isSuccess = filePath.mkdirs();
        return isSuccess ? filePath : null;
    }

    /**
     * 获取文件
     *
     * @param context 如存到缓存目录，不能为空
     * @param isCache 是否存到缓存目录
     * @param name    文件名称
     * @return 目录路径file 内存卡不存在和文件夹创建错误会返回null
     */
    public static File getFile(Context context, boolean isCache, String name) {
        File dirFile;
        if (isCache) {
            dirFile = context.getExternalCacheDir();
        } else {
            dirFile = getSdCardPath();
        }
        if (dirFile == null)
            return null;
        File filePath = new File(dirFile, name);

        boolean isSuccess = true;
        if (!filePath.exists())
            isSuccess = filePath.mkdirs();
        return isSuccess ? filePath : null;
    }

    /**
     * 获取文件大小
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                // 如果下面还有文件
                if (f.isDirectory())
                    size = size + getFolderSize(f);
                else
                    size = size + f.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取图片压缩类型 jpg或png
     *
     * @param imageName 图片名称(可以是整个url或uri)
     */
    public static Bitmap.CompressFormat getImageCompressFormat(String imageName) {
        if (!TextUtils.isEmpty(imageName) && imageName.toLowerCase().endsWith(".png"))
            return Bitmap.CompressFormat.PNG;
        return Bitmap.CompressFormat.JPEG;
    }

    /**
     * 获取文件类型string
     */
    private static String getFileTypeName(FileType fileType) {
        switch (fileType) {
            case IMAGE:
                return "image";
            case VIDEO:
                return "video";
            default:
                return "file";
        }
    }

    /**
     * 获取app本地存放所有文件的父目录File
     *
     * @return 文件路径file 内存卡不存在和文件夹创建错误会返回null
     */
    private static File getAppLocalFileParentPath() {
        File sdCardPath = getSdCardPath();
        if (sdCardPath == null)
            return null;
        File filePath = new File(sdCardPath, "/healthmall/file/");

        boolean isSuccess = true;
        if (!filePath.exists())
            isSuccess = filePath.mkdirs();
        return isSuccess ? filePath : null;
    }

    /**
     * 创建一个新文件
     *
     * @param fileType       文件类型 例如：FileType.IMAGE
     * @param fileExtensions 文件扩展名 例如：jpg
     * @param isCacheFile    是否放到缓存目录（缓存目录可以随着app清除，没特殊存在意义的建议true）
     * @param context        context 缓存文件时不可空
     * @return 新文件 内存卡不存在和文件创建错误会返回null
     */
    public static File createNewFile(FileType fileType, String fileExtensions, boolean isCacheFile, Context context) {
        File filePath;
        if (!isCacheFile) filePath = getAppLocalFilePath(fileType);
        else filePath = getAppLocalCacheFilePath(context, fileType);

        if (filePath == null) return null;
        File file = new File(filePath, String.valueOf(System.currentTimeMillis()) + "." + fileExtensions);

//        Lmsg.e("file-->>" + file.getAbsolutePath());
        if (file.exists())
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
