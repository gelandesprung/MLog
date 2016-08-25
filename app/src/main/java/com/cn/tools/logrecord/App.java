package com.cn.tools.logrecord;

import android.app.Application;

import java.text.SimpleDateFormat;

import com.cn.tools.mlog.LogLevel;
import com.cn.tools.mlog.MLogSettings;
import com.cn.tools.mlog.WorkMode;

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
                .logPath("ab").logPrefix("@@").fileNameFormat(new SimpleDateFormat("yyyy-MM-dd-HH-mm")).init(this);
    }
}
