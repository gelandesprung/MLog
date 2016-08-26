package com.cn.tools.logrecord;

import android.app.Application;

import java.text.SimpleDateFormat;

import com.cn.tools.mlog.LogLevel;
import com.cn.tools.mlog.MLogSettings;
import com.cn.tools.mlog.WorkMode;
import com.cn.tools.mlog.formatter.DefaultLogFormatter;
import com.cn.tools.mlog.formatter.LogFormatter;
import com.cn.tools.mlog.formatter.MarkdownFormatter;

/**
 * ${TODO} What the class does
 *
 * @author： yanchao
 * @date： 8/3/16-9:14 AM
 * ${TAGS}
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MLogSettings.getInstance().workMode(WorkMode.ALL).logLevel(LogLevel.WARN).jsonLog(true).xmlLog(true)
                .logFormatter(new DefaultLogFormatter())
                .logPath("ab").fileNamePrefix("@@").fileNameSuffix("txt").fileNameFormat(new SimpleDateFormat("yyyy-MM-dd-HH-mm")).init(this);
    }
}
