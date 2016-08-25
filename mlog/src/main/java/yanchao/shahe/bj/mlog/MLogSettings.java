package yanchao.shahe.bj.mlog;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * MLog的输出配置管理类，单例模式
 *
 * @author： yanchao
 * @date： 8/4/16-9:55 AM
 * ${TAGS}
 */
public class MLogSettings {
    private static String mSdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
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
    /**
     * 当前打开的日志文件名
     */
    private static String fileName = "";
    /**
     * 缓存队列的容量
     */
    private static final int CAPACITY = 2000;
    /**
     * 缓存需要处理的日志
     */
    private static BlockingQueue<MessageWrapper> msgQueue = new ArrayBlockingQueue<>(CAPACITY);
    private static FileWriter mFileWrite;

    boolean print_to_console = getWorkMode() == WorkMode.CONSOLE || getWorkMode() == WorkMode.ALL;
    boolean write_to_file = getWorkMode() == WorkMode.ALL || getWorkMode() == WorkMode.FILE;
    private static MLogSettings instance = new MLogSettings();

    private MLogSettings() {
    }

    public static MLogSettings getInstance() {
        return instance;
    }

    public String getSdcard() {
        return mSdcard;
    }

    public void init(Context v_context) {
        mSdcard = v_context.getExternalCacheDir().getAbsolutePath();
        if (MLogSettings.getInstance().getWorkMode() != WorkMode.NONE)
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        try {
                            MessageWrapper msg = msgQueue.take();
                            if (print_to_console) {
                                Log.d(msg.getTag(), msg.getMsg());
                            }
                            if (write_to_file) {
                                if (!fileName.equals(MLogSettings.getInstance().getFileNameFormat().format(new Date()
                                ))) {
                                    fileName = MLogSettings.getInstance().getFileNameFormat().format(new Date());
                                    if (mFileWrite != null) {
                                        mFileWrite.close();
                                        mFileWrite = null;
                                    }
                                }
                                if (mFileWrite == null) {
                                    File dir = new File(MLogSettings.getInstance().getSdcard() + File.separator +
                                            MLogSettings
                                            .getInstance().getLogPath());
                                    if (!dir.exists()) {
                                        dir.mkdirs();
                                    }
                                    File log = new File(dir.getAbsolutePath() + File.separator + MLogSettings
                                            .getInstance()
                                            .getLogPrefix() + fileName + ".log");
                                    if (!log.exists()) {
                                        log.createNewFile();
                                        // FIXME: 8/4/16 删除旧文件
                                    }
                                    mFileWrite = new FileWriter(log, true);
                                }
                                mFileWrite.write(msg.getMsg());
                                if (msgQueue.remainingCapacity() < CAPACITY) {
                                    ArrayList<MessageWrapper> cache = new ArrayList<>();
                                    msgQueue.drainTo(cache);
                                    for (MessageWrapper log : cache) {
                                        mFileWrite.write(log.getMsg());
                                    }
                                }
                                mFileWrite.flush();
                            }

                        } catch (InterruptedException v_e) {
                            v_e.printStackTrace();
                        } catch (IOException v_e) {
                            v_e.printStackTrace();
                        }
                    }
                }
            }).start();
    }

    public static BlockingQueue<MessageWrapper> getMsgQueue() {
        return msgQueue;
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

    public MLogSettings mLogLevel(LogLevel val) {
        mLogLevel = val;
        return this;
    }

    public LogLevel getLogLevel() {
        return mLogLevel;
    }

    public MLogSettings mJsonLog(boolean val) {
        mJsonLog = val;
        return this;
    }

    public MLogSettings mXmlLog(boolean val) {
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
