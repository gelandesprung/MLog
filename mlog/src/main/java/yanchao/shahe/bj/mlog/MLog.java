package yanchao.shahe.bj.mlog;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 包装日志打印功能，侧重于日志的保存
 * 用法：
 * 在程序启动之后，打印日志之前需要进行初始化：
 *
 * @author： yanchao
 * @date： 8/4/16-9:49 AM
 * @see MLogSettings
 * <p/>
 * MLogSettings.getInstance()
 * .mWorkMode(WorkMode.ALL)
 * .mLogLevel(LogLevel.WARN)
 * .mJsonLog(true)
 * .mXmlLog(true)
 * .fileNameFormat("yyyy-MM-dd")
 * .mLogPath("ab")
 * .mLogPrefix("@@");
 * <p/>
 * 在某一个类中打印日志时先要构造MLog对象
 * MLog mMLog = MLog.getMLog(MainActivity.class.getSimpleName());
 * getMLog(param)只是需要一个作为tag的参数，建议使用类名
 * mMLog.d(xxxx)打印就可以了
 * <p/>
 * <p/>
 * 现在的日志是按天写入文件，如果把fileNameFormat改成"yyyy-MM-dd-HH"就是一个小时换一个文件写了。
 */
public class MLog {
    /**
     * json格式化的缩进
     */
    private static final int JSON_INDENT = 4;
    private String tag;
    private static FileWriter mFileWrite;
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
    private static BlockingQueue<String> msgQueue = new ArrayBlockingQueue<>(CAPACITY);
    /**
     * 处理xml格式化
     */
    private static ExecutorService ioThread = Executors.newFixedThreadPool(10);


    boolean print_to_console = MLogSettings.getInstance().getWorkMode() == WorkMode.CONSOLE ||
            MLogSettings.getInstance().getWorkMode() == WorkMode.ALL;
    boolean write_to_file = MLogSettings.getInstance().getWorkMode() == WorkMode.ALL || MLogSettings
            .getInstance().getWorkMode() == WorkMode.FILE;
    /**
     * 是否打开日志功能
     */
    boolean active = MLogSettings.getInstance().getWorkMode() != WorkMode.NONE;

    {
        if (active)
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        try {
                            String msg = msgQueue.take();
                            if (print_to_console) {
                                Log.d(tag, msg);
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
                                    File dir = new File(MLogSettings.getInstance().getSdcard() + File.separator + MLogSettings
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
                                mFileWrite.write(msg);
                                if (msgQueue.remainingCapacity() < CAPACITY) {
                                    ArrayList<String> cache = new ArrayList<>();
                                    msgQueue.drainTo(cache);
                                    for (String log : cache) {
                                        mFileWrite.write(log);
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

    private MLog(String v_tag) {
        tag = v_tag;
    }

    public static MLog getMLog(String tag) {
        return new MLog(tag);
    }

    public void v(String msg) {
        print(LogLevel.VERBOSE, headTag().append(msg).append('\n').toString());
    }

    public void v(String format, Object... args) {
        print(LogLevel.VERBOSE, headTag().append(String.format(format, args)).append('\n').toString());
    }

    public void d(String msg) {
        print(LogLevel.DEBUG, headTag().append(msg).append('\n').toString());
    }

    public void d(String format, Object... args) {
        print(LogLevel.DEBUG, headTag().append(String.format(format, args)).append('\n').toString());
    }

    public void i(String msg) {
        print(LogLevel.INFO, headTag().append(msg).append('\n').toString());
    }

    public void i(String format, Object... args) {
        print(LogLevel.INFO, headTag().append(String.format(format, args)).append('\n').toString());
    }

    public void w(String msg) {
        print(LogLevel.WARN, headTag().append(msg).append('\n').toString());
    }

    public void w(String format, Object... args) {
        print(LogLevel.WARN, headTag().append(String.format(format, args)).append('\n').toString());
    }

    public void e(String msg) {
        print(LogLevel.ERROR, headTag().append(msg).append('\n').toString());
    }

    public void e(String format, Object... args) {
        print(LogLevel.ERROR, headTag().append(String.format(format, args)).append('\n').toString());
    }

    public void a(String msg) {
        print(LogLevel.ASSERT, headTag().append(msg).append('\n').toString());
    }

    public void a(String format, Object... args) {
        print(LogLevel.ASSERT, headTag().append(String.format(format, args)).append('\n').toString());
    }

    public void xml(final String msg) {
        ioThread.execute(new Runnable() {
            @Override
            public void run() {
                print(LogLevel.XML, headTag().append(formatXML(msg)).append('\n').toString());
            }
        });
    }

    public void json(String msg) {
        print(LogLevel.JSON, headTag().append(formatJson(msg)).append('\n').toString());
    }

    private void print(LogLevel level, String v_s) {
        if (active
                && (level.getLevel() >= MLogSettings.getInstance().getLogLevel().getLevel()
                || (MLogSettings.getInstance().isJsonLog() && level == LogLevel.JSON)
                || (MLogSettings.getInstance().isXmlLog() && level == LogLevel.XML))) {
            msgQueue.offer(v_s);
        }
    }

    public static String formatJson(String message) {
        String json;
        try {
            if (message.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(message);
                json = jsonObject.toString(JSON_INDENT);
            } else if (message.startsWith("{")) {
                JSONArray jsonArray = new JSONArray(message);
                json = jsonArray.toString(JSON_INDENT);
            } else {
                json = message;
            }
        } catch (JSONException e) {
            json = message;
        }
        return "```\n" + json + "\n```";
    }

    /**
     * 格式化xml
     * 这段代码被到处抄，当然我也是抄的，
     * 不过我在使用的时候发现它格式化的速度比较慢，所以我把它丢到线程里执行
     *
     * @param inputXML
     * @return
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String formatXML(String inputXML) {
        String output = inputXML;
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            output = xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "```\n" + output + "\n```";
    }

    private static String method() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        return caller.getMethodName();
    }

    private StringBuilder headTag() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        StringBuilder builder = new StringBuilder();
        builder.append("## ").append(MLogSettings.getInstance().getDateFormat().format(new Date())).append('\n');
        builder.append('>').append(tag).append(": ").append(caller.getMethodName()).append("\n\n");
        return builder;
    }
}
