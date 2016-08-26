package com.cn.tools.mlog.formatter;

import android.util.Log;

import com.cn.tools.mlog.LogLevel;
import com.cn.tools.mlog.MLogSettings;
import com.cn.tools.mlog.WorkMode;

/**
 * ${TODO} What the class does
 *
 * @author： yanchao
 * @date： 8/25/16-3:05 PM
 * ${TAGS}
 */
public abstract class LogFormatter {
    public abstract String formate(LogLevel level, String tag, String caller, String msg);

    protected void printToConsole(LogLevel level,String tag, String msg) {
        WorkMode workMode = MLogSettings.getInstance().getWorkMode();
        if (workMode == WorkMode.ALL || workMode == WorkMode.CONSOLE) {
            if (level == LogLevel.ASSERT) {
                Log.wtf(tag, msg);
            } else if (level == LogLevel.VERBOSE) {
                Log.v(tag, msg);
            } else if (level == LogLevel.DEBUG) {
                Log.d(tag, msg);
            } else if (level == LogLevel.INFO) {
                Log.i(tag, msg);
            } else if (level == LogLevel.ERROR) {
                Log.e(tag, msg);
            } else if (level == LogLevel.JSON) {
                Log.v(tag, msg);
            } else if (level == LogLevel.XML) {
                Log.v(tag, msg);
            }
        }
    }
}
