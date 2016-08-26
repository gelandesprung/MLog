package com.cn.tools.mlog.formatter;

import com.cn.tools.mlog.LogLevel;
import com.cn.tools.mlog.MLog;
import com.cn.tools.mlog.MLogSettings;

import java.util.Date;

/**
 * ${TODO} What the class does
 *
 * @author： yanchao
 * @date： 8/25/16-4:25 PM
 * ${TAGS}
 */
public class MarkdownFormatter extends LogFormatter {
    @Override
    public String formate(LogLevel level, String tag, String caller, String msg) {
        String formatedMsg;
        if (level == LogLevel.XML) {
            formatedMsg = MLog.formatXML(msg);
        } else if (level == LogLevel.JSON) {
            formatedMsg = MLog.formatJson(msg);
        } else {
            formatedMsg = msg;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("## ").append(MLogSettings.getInstance().getDateFormat().format(new Date())).append('\n');
        builder.append('>').append(tag).append(": ").append(caller).append("\n\n");
        builder.append("```\n").append(formatedMsg).append("\n```").append('\n');
        String result = builder.toString();
        printToConsole(level, tag, result);
        return result;
    }

}
