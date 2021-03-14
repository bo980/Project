package com.school.project.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class OSHelper {
    public static final int OS_TYPE_MIUI = -1000;
    public static final int OS_TYPE_FLYME = -2000;
    public static final int OS_TYPE_HUAWEI = -3000;
    public static final int OS_TYPE_DEF = 0;
    public static final int OS_CODE_INSTALL = 1010;

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";


    public int getOsType() {
        return mOsType;
    }

    private boolean mIsInitialization;

    public void setOsType(int mOsType) {
        this.mOsType = mOsType;
        mIsInitialization = true;
    }

    public boolean isInitialization() {
        return mIsInitialization;
    }

    private int mOsType = 0;

    public static OSHelper getInstance() {
        return OSHelperCreate.instance;
    }

    private static class OSHelperCreate {
        private static final OSHelper instance = new OSHelper();
    }

    private OSHelper() {
    }

    /**
     * 打开指定包名的App应用信息界面
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void openAppInfo(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 可用来做App信息分享
     *
     * @param context 上下文
     * @param info    分享信息
     */
    public static void shareAppInfo(Context context, String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        context.startActivity(intent);
    }

    /**
     * 打开App设置中心
     *
     * @param context 上下文
     */
    public static void openAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 获取app meta data
     *
     * @param ctx 上下文
     * @param key key
     * @return meta data
     */
    public static String getAppMetaData(Context ctx, final String key) {
        try {
            ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(),
                    PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void gotoPermissionSetting(Context ctx) {
        if (OSHelper.getInstance().isInitialization()) {
            switch (OSHelper.getInstance().getOsType()) {
                case OSHelper.OS_TYPE_FLYME:
                    gotoMeizuPermission(ctx);
                    break;
                case OSHelper.OS_TYPE_MIUI:
                    gotoMIUIPermission(ctx);
                    break;
                case OSHelper.OS_TYPE_HUAWEI:
                    gotoHuaweiPermission(ctx);
                    break;
                default:
                    ctx.startActivity(getAppDetailSettingIntent(ctx));
                    break;
            }
        } else {
            if (OSHelper.isFlyme()) {
                OSHelper.getInstance().setOsType(OSHelper.OS_TYPE_FLYME);
                gotoMeizuPermission(ctx);
            } else if (OSHelper.isMIUI()) {
                OSHelper.getInstance().setOsType(OSHelper.OS_TYPE_MIUI);
                gotoMIUIPermission(ctx);
            } else if (OSHelper.isEMUI()) {
                OSHelper.getInstance().setOsType(OSHelper.OS_TYPE_HUAWEI);
                gotoHuaweiPermission(ctx);
            } else {
                ctx.startActivity(getAppDetailSettingIntent(ctx));
                OSHelper.getInstance().setOsType(OSHelper.OS_TYPE_DEF);
            }
        }
    }

    /**
     * 跳转到miui的权限管理页面
     */
    public void gotoMIUIPermission(Context ctx) {
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        i.setComponent(componentName);
        i.putExtra("extra_pkgname", ctx.getPackageName());
        try {
            ctx.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            gotoMeizuPermission(ctx);
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private void gotoMeizuPermission(Context ctx) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", ctx.getPackageName());
        try {
            ctx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            gotoHuaweiPermission(ctx);
        }
    }

    /**
     * 华为的权限管理页面
     */
    private void gotoHuaweiPermission(Context ctx) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            ctx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.startActivity(getAppDetailSettingIntent(ctx));
        }

    }

    /**
     * 获取应用详情页面intent
     *
     * @param ctx
     * @return
     */
    private Intent getAppDetailSettingIntent(Context ctx) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", ctx.getPackageName(), null));
        return localIntent;
    }


    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }


    /**
     * 获取设备主机地址
     *
     * @return 设备主机地址
     */
    public static String getDeviceHost() {
        return Build.HOST;
    }

    /**
     * 获取设备的唯一标识。由设备的多个信息拼接合成
     *
     * @return 设备唯一标识
     */
    public static String getDeviceFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 获取设备id
     *
     * @return 设备id
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备Mac地址
     *
     * @return 设备Mac
     */
    public static String getDeviceMac(Context context) {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] address = networkInterface.getHardwareAddress();
            for (byte b : address) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getIP(Context context) {
        try {
            String ipv;
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
                        ipv = address.getHostAddress();
                        return ipv;
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.e("localip", ex.toString());
        }
        return "127.0.0.1";
    }


    /**
     * 判断手机是否是魅族
     *
     * @return
     */
    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }


    /**
     * 判断手机是否是华为
     *
     * @return
     */
    public static boolean isEMUI() {
        return isPropertiesExist(KEY_EMUI_VERSION_CODE);
    }

    /**
     * 判断手机是否是小米
     */
    public static boolean isMIUI() {
        return isPropertiesExist(KEY_MIUI_VERSION_CODE, KEY_MIUI_VERSION_NAME, KEY_MIUI_INTERNAL_STORAGE);
    }

    private static boolean isPropertiesExist(String... keys) {
        if (keys == null || keys.length == 0) {
            return false;
        }
        try {
            BuildProperties properties = BuildProperties.newInstance();
            for (String key : keys) {
                String value = properties.getProperty(key);
                if (value != null)
                    return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static final class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            // 读取系统配置信息build.prop类
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }
    }


    /**
     * 兼容8.0安装位置来源的权限
     */
    public static void installApp(Activity activity, String apkPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //是否有安装位置来源的权限
            boolean haveInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {
                //L.i("8.0手机已经拥有安装未知来源应用的权限，直接安装！");
                installApk(activity, apkPath);
            } else {
                Uri packageUri = Uri.parse("package:" + activity.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                activity.startActivityForResult(intent, OS_CODE_INSTALL);
            }
        } else {
            installApk(activity, apkPath);
        }
    }

    private static void installApk(Context context, String apkPath) {
        context.startActivity(installIntent(context, apkPath));
    }

    private static Intent installIntent(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(apkPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }

        return intent;
    }


    //下载apk
    public static long downloadAPK(Context context, String url, String title, String content, String subPath) {

        long downloadId = 0;
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription(content);
        File file = new File(subPath);
        request.setDestinationUri(Uri.fromFile(file));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE
                | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            Logger.e("downloadAPK...");
            downloadId = downloadManager.enqueue(request);
        }

        return downloadId;
    }
}

