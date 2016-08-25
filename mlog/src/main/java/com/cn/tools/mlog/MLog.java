package com.cn.tools.mlog;

import android.annotation.TargetApi;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
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


    /**
     * 处理xml格式化
     */
    private static ExecutorService ioThread = Executors.newFixedThreadPool(10);


    private MLog(String v_tag) {
        tag = v_tag;
    }

    public static MLog getMLog(String tag) {
        return new MLog(tag);
    }

    public void v(String msg) {
        print(LogLevel.VERBOSE, new MessageWrapper(tag, headTag().append(msg).append('\n').toString()));
    }

    public static void v(String tag, String msg) {
        _print(LogLevel.VERBOSE, new MessageWrapper(tag, headTag(tag, method()).append(msg).append('\n').toString()));
    }

    public void v(String format, Object... args) {
        print(LogLevel.VERBOSE, new MessageWrapper(tag, headTag().append(String.format(format, args)).append('\n')
                .toString()));
    }

    public static void v(String tag, String format, Object... args) {
        _print(LogLevel.VERBOSE, new MessageWrapper(tag, headTag(tag, method()).append(String.format(format, args))
                .append('\n').toString()));
    }

    public void d(String msg) {
        print(LogLevel.DEBUG, new MessageWrapper(tag, headTag().append(msg).append('\n').toString()));
    }

    public static void d(String tag, String msg) {
        _print(LogLevel.DEBUG, new MessageWrapper(tag, headTag(tag, method()).append(msg).append('\n').toString()));
    }

    public void d(String format, Object... args) {
        print(LogLevel.DEBUG, new MessageWrapper(tag, headTag().append(String.format(format, args)).append('\n')
                .toString()));
    }

    public static void d(String tag, String format, Object... args) {
        _print(LogLevel.DEBUG, new MessageWrapper(tag, headTag(tag, method()).append(String.format(format, args))
                .append('\n').toString()));
    }

    public void i(String msg) {
        print(LogLevel.INFO, new MessageWrapper(tag, headTag().append(msg).append('\n').toString()));
    }

    public static void i(String tag, String msg) {
        _print(LogLevel.INFO, new MessageWrapper(tag, headTag(tag, method()).append(msg).append('\n').toString()));
    }

    public void i(String format, Object... args) {
        print(LogLevel.INFO, new MessageWrapper(tag, headTag().append(String.format(format, args)).append('\n')
                .toString()));
    }

    public static void i(String tag, String format, Object... args) {
        _print(LogLevel.INFO, new MessageWrapper(tag, headTag(tag, method()).append(String.format(format, args))
                .append('\n').toString()));
    }

    public void w(String msg) {
        print(LogLevel.WARN, new MessageWrapper(tag, headTag().append(msg).append('\n').toString()));
    }

    public static void w(String tag, String msg) {
        _print(LogLevel.WARN, new MessageWrapper(tag, headTag(tag, method()).append(msg).append('\n').toString()));
    }

    public void w(String format, Object... args) {
        print(LogLevel.WARN, new MessageWrapper(tag, headTag().append(String.format(format, args)).append('\n')
                .toString()));
    }

    public static void w(String tag, String format, Object... args) {
        _print(LogLevel.WARN, new MessageWrapper(tag, headTag(tag, method()).append(String.format(format, args))
                .append('\n').toString()));
    }

    public void e(String msg) {
        print(LogLevel.ERROR, new MessageWrapper(tag, headTag().append(msg).append('\n').toString()));
    }

    public static void e(String tag, String msg) {
        _print(LogLevel.ERROR, new MessageWrapper(tag, headTag(tag, method()).append(msg).append('\n').toString()));
    }

    public void e(String format, Object... args) {
        print(LogLevel.ERROR, new MessageWrapper(tag, headTag().append(String.format(format, args)).append('\n')
                .toString()));
    }

    public static void e(String tag, String format, Object... args) {
        _print(LogLevel.ERROR, new MessageWrapper(tag, headTag(tag, method()).append(String.format(format, args))
                .append('\n').toString()));
    }

    public void a(String msg) {
        print(LogLevel.ASSERT, new MessageWrapper(tag, headTag().append(msg).append('\n').toString()));
    }

    public static void a(String tag, String msg) {
        _print(LogLevel.ASSERT, new MessageWrapper(tag, headTag(tag, method()).append(msg).append('\n').toString()));
    }

    public void a(String format, Object... args) {
        print(LogLevel.ASSERT, new MessageWrapper(tag, headTag().append(String.format(format, args)).append('\n')
                .toString()));
    }

    public static void a(String tag, String format, Object... args) {
        _print(LogLevel.ASSERT, new MessageWrapper(tag, headTag(tag, method()).append(String.format(format, args))
                .append('\n').toString()));
    }

    public void xml(final String msg) {
        ioThread.execute(new Runnable() {
            @Override
            public void run() {
                print(LogLevel.XML, new MessageWrapper(tag, headTag().append(formatXML(msg)).append('\n').toString()));
            }
        });
    }

    public static void xml(final String tag, final String msg) {
        final String method = method();
        ioThread.execute(new Runnable() {
            @Override
            public void run() {
                _print(LogLevel.XML, new MessageWrapper(tag, headTag(tag, method).append(formatXML(msg)).append
                        ('\n').toString()));
                }
        });
    }

    public void json(String msg) {
        print(LogLevel.JSON, new MessageWrapper(tag, headTag().append(formatJson(msg)).append('\n').toString()));
    }

    public static void json(String tag, String msg) {
        _print(LogLevel.JSON, new MessageWrapper(tag, headTag(tag, method()).append(formatJson(msg)).append('\n')
                .toString()));
    }

    private void print(LogLevel level, MessageWrapper v_s) {
        _print(level, v_s);
    }

    private static void _print(LogLevel level, MessageWrapper v_s) {
        if (MLogSettings.getInstance().getWorkMode() != WorkMode.NONE
                && (level.getLevel() >= MLogSettings.getInstance().getLogLevel().getLevel()
                || (MLogSettings.getInstance().isJsonLog() && level == LogLevel.JSON)
                || (MLogSettings.getInstance().isXmlLog() && level == LogLevel.XML))) {
            MLogSettings.getInstance().getMsgQueue().offer(v_s);
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
        return headTag(tag, method());
    }

    private static StringBuilder headTag(String v_tag, String method_name) {
        StringBuilder builder = new StringBuilder();
        builder.append("## ").append(MLogSettings.getInstance().getDateFormat().format(new Date())).append('\n');
        builder.append('>').append(v_tag).append(": ").append(method_name).append("\n\n");
        return builder;
    }
}
