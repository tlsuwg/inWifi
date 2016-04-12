package com.hxuehh.reuse_Process_Imp.staicUtil.store.file;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.ApiUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePathDCIM = "";
    private static final String DST_FOLDER_NAMEDCIM = "DCIM/Camera";

    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "/zhenCC9Camera";

    private static String initPath() {
        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    //    /mnt/sdcard/DCIM/Camera/
//    没有sd 卡的话，是直接在 DCIM/Camera
    public static String saveBitmap(Bitmap b) throws IOException {
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";

        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        FileOutputStream fout = new FileOutputStream(jpegName);
        BufferedOutputStream bos = new BufferedOutputStream(fout);
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return jpegName;
    }


    public static String initPathDCIM() {
        if (storagePathDCIM.equals("")) {
            storagePathDCIM = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAMEDCIM;
            File f = new File(storagePathDCIM);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePathDCIM;
    }

    public static String saveBitmapDCIM(Bitmap b) throws IOException {
        String path = initPathDCIM();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        FileOutputStream fout = new FileOutputStream(jpegName);
        BufferedOutputStream bos = new BufferedOutputStream(fout);
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return jpegName;
    }

    /**
     * 存图片到相册 add by ybj
     */

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String uris = "";
        // 其次把文件插入到系统图库
        try {
            uris = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));


        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + uris)));
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(uris)));
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));

    }


    private String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }


    public static boolean saveImgToGallery(Context context, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在

        if (!sdCardExist) return false;
        try {
            ContentValues values = new ContentValues();
            values.put("datetaken", new Date().toString());
            values.put("mime_type", "image/jpg");
            values.put("_data", file.getAbsolutePath());
            ContentResolver cr = context.getContentResolver();
            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + file.getParentFile().getAbsolutePath()}, null, null);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return true;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    /**
     * 删除文件，可以是单个文件或文件夹
     *
     * @param fileName 待删除的文件名
     * @return 文件删除成功返回true, 否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param dir 被删除目录的文件路径
     * @return 目录删除成功返回true, 否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            return false;
        }

        // 删除当前目录
        return dirFile.delete();
    }

    public static String getMIMEType(String filePath) {
        File f = new File(filePath);
        return getMIMEType(f);
    }

    /**
     * 获取文件的MIME type
     *
     * @param f
     * @return
     */
    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

		/* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        /* 如果无法直接打开，就跳出软件列表给用户选择 */
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }


    public static String getAppFilesPath() {
        return SuApplication.getInstance().getFilesDir().getAbsolutePath();
    }


    public static File getDiskCacheDir(Context context) {
        File externalFile = getExternalCacheDir(context);
        if (!externalFile.exists()) {
            externalFile = context.getCacheDir();
        }

        return externalFile;
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        File externalFile = getExternalCacheDir(context);
        if (!externalFile.exists()) {
            externalFile = context.getCacheDir();
        }

        return new File(externalFile, uniqueName);
    }

    public static File getExternalCacheDir(Context context) {
        File cacheDir = null;
        if (ApiUtil.hasFroyo() && context != null) {
            cacheDir = context.getExternalCacheDir();
        }

        if (cacheDir == null) {
            String cacheDirPath = "/Android/data/" + context.getPackageName() + "/cache/";
            cacheDir = new File(Environment.getExternalStorageDirectory(), cacheDirPath);
        }

        return cacheDir;
    }


    //获得指定文件的byte数组
    public static byte[] getBytes(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        byte[] buffer = bos.toByteArray();
        return buffer;
    }


    public static void doFileCopy(String from, File fileTo) throws IOException {
        File file = new File(from);
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(fileTo);
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            fos.write(b, 0, n);
        }
        fis.close();
        fos.close();
    }

    public static byte[] getBytesFront(String filePath,int size) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] b = new byte[size];
        fis.read(b);
        fis.close();
        return b;
    }

    //根据byte数组，生成文件
    public static File getFileExist(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = getFileExist(filePath,fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File getFileExist(String filePath, String fileName) {
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return file;
    }


    public static StatFs SDCardSizeStatFs() {
        String sDcString = Environment.getExternalStorageState();
        if (sDcString.equals(Environment.MEDIA_MOUNTED)) {
            // 取得sdcard文件路径
            File pathFile = android.os.Environment
                    .getExternalStorageDirectory();
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            return statfs;
        }
        return null;
    }

    public static String getInfo(StatFs statfs) {
        // 获取SDCard上BLOCK总数
        long nTotalBlocks = statfs.getBlockCount();
        // 获取SDCard上每个block的SIZE
        long nBlocSize = statfs.getBlockSize();
        // 获取可供程序使用的Block的数量
        long nAvailaBlock = statfs.getAvailableBlocks();
        // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
        long nFreeBlock = statfs.getFreeBlocks();

        // 计算SDCard 总容量大小MB
        long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;
        // 计算 SDCard 剩余大小MB
        long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;

        return "总容量" + nSDTotalSize + "MB;剩余" + nSDFreeSize + "MB";
    }

    public static StatFs readSystem() {
        File root = Environment.getRootDirectory();
        return new StatFs(root.getPath());
    }


    public static long getFreeBytes(StatFs statfs) {
        if (statfs != null) {
            long nBlocSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
            long nFreeBlock = statfs.getFreeBlocks();

            return nBlocSize * nFreeBlock;
        }
        return 0;
    }

    public static long readSDFree() {
        return getFreeBytes(SDCardSizeStatFs());
    }

    public static long readSystemFree() {
        return getFreeBytes(readSystem());
    }



    public static byte[] readAudioFile(Context context, String filename) {


        try {
            InputStream ins = context.getAssets().open(filename);
            byte[] data = new byte[ins.available()];

            ins.read(data);
            ins.close();

            return data;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


}
