package com.sixtofly.entity;

import org.apache.commons.lang3.StringUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * @describe:（类描述）
 * @author：（创建人）xyb
 * @creationTime：（创建时间）2018-12-0512:50
 * @modifier：（修改人）
 * @modificationTime：（修改时间）
 * @修改备注：
 * @Note:
 */
public class Constant {
    /**
     * 桌面路径
     */
    public static String DESKTOP_PAHT;
    /**
     * 当前目录
     */
    public static String CURRENT_DIR;

    static {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File file = fsv.getHomeDirectory();
        DESKTOP_PAHT = file.getPath();
        CURRENT_DIR = System.getProperty("user.dir");
    }
}
