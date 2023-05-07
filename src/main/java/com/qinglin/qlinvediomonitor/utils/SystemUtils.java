package com.qinglin.qlinvediomonitor.utils;

import java.io.File;

/**
 * @author by shoulaxiao
 * @version 1.0.0
 * @Classname SystemUtils
 * @Description
 * @date 2023/5/3 10:49
 */
public class SystemUtils {

    public static String WEB_STATIC_IMG = null;
    public static String WEB_STATIC_IMG_LOCAL = null;

    /**
     * 判断操作系统是否是 Windows
     *
     * @return true：操作系统是 Windows
     * false：其它操作系统
     */
    public static boolean isWindows() {
        String osName = getOsName();
        return osName != null && osName.startsWith("Windows");
    }

    public static String prefixPath() {
        File[] parts = File.listRoots();
        File path = parts[parts.length - 1];
        if (isWindows()){
            return path.getAbsolutePath() + "temp\\";
        }else {
            return path.getAbsolutePath()+"temp/";
        }
    }


    /**
     * 判断操作系统是否是 MacOS
     *
     * @return true：操作系统是 MacOS
     * false：其它操作系统
     */
    public static boolean isMacOs() {
        String osName = getOsName();

        return osName != null && osName.startsWith("Mac");
    }

    /**
     * 判断操作系统是否是 Linux
     *
     * @return true：操作系统是 Linux
     * false：其它操作系统
     */
    public static boolean isLinux() {
        String osName = getOsName();

        return (osName != null && osName.startsWith("Linux")) || (!isWindows() && !isMacOs());
    }

    /**
     * 获取操作系统名称
     *
     * @return os.name 属性值
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static void load() {
        String pathPrefix = System.getProperty("user.dir");
        if (isWindows()){
            WEB_STATIC_IMG_LOCAL = pathPrefix + "\\src\\main\\resources\\static\\video\\preViewImages\\";
        }else {
            WEB_STATIC_IMG_LOCAL = pathPrefix + "/src/main/resources/static/video/preViewImages/";
        }
    }
}