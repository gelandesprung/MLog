package yanchao.shahe.bj.mlog;

import android.os.Environment;

import java.text.SimpleDateFormat;

/**
 * MLog的输出配置管理类，单例模式
 *
 * @author： yanchao
 * @date： 8/4/16-9:55 AM
 * ${TAGS}
 */
public class MLogSettings {
    private static final String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * 日志模块工作的模式，有四种：
     * 只输出到终端，只输出到文件，既输出到终端又输出到文件，关闭日志模式
     */
    private WorkMode mWorkMode = WorkMode.ALL;
    /**
     * 日志文件名的前缀
     */
    private String mLogPrefix = "";
    /**
     * 日志只在的路径
     */
    private String mLogPath = "richfit/cache/";
    /**
     * 日志名日志的样式
     */
    private SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 日志条目的日期样式
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 设置日志输出的级别
     */
    private LogLevel mLogLevel = LogLevel.DEBUG;
    /**
     * 是否输出json日志
     */
    private boolean mJsonLog = false;
    /**
     * 是否输出xml日志
     */
    private boolean mXmlLog = false;

    private static MLogSettings instance = new MLogSettings();

    private MLogSettings() {
    }

    public static MLogSettings getInstance() {
        return instance;
    }

    public static String getSdcard() {
        return sdcard;
    }

    public WorkMode getWorkMode() {
        return mWorkMode;
    }

    public String getLogPrefix() {
        return mLogPrefix;
    }

    public String getLogPath() {
        return mLogPath;
    }

    public SimpleDateFormat getFileNameFormat() {
        return fileNameFormat;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }


    public MLogSettings mWorkMode(WorkMode val) {
        mWorkMode = val;
        return this;
    }

    public MLogSettings mLogPrefix(String val) {
        mLogPrefix = val;
        return this;
    }

    public MLogSettings fileNameFormat(SimpleDateFormat val) {
        fileNameFormat = val;
        return this;
    }

    public MLogSettings dateFormat(SimpleDateFormat val) {
        dateFormat = val;
        return this;
    }

    public MLogSettings mLogPath(String val) {
        mLogPath = val;
        return this;
    }
    public MLogSettings mLogLevel(LogLevel val){
        mLogLevel = val;
        return this;
    }

    public LogLevel getLogLevel() {
        return mLogLevel;
    }
    public MLogSettings mJsonLog(boolean val){
        mJsonLog = val;
        return this;
    }
    public MLogSettings mXmlLog(boolean val){
        mXmlLog = val;
        return this;
    }

    public boolean isJsonLog() {
        return mJsonLog;
    }

    public boolean isXmlLog() {
        return mXmlLog;
    }
}
