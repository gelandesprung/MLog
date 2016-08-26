package com.cn.tools.mlog.formatter;

import com.cn.tools.mlog.LogLevel;

/**
 * ${TODO} What the class does
 *
 * @author： yanchao
 * @date： 8/25/16-3:14 PM
 * ${TAGS}
 */
public class DefaultLogFormatter extends LogFormatter {
    @Override
    public String formate(LogLevel level,String tag, String caller, String msg) {
        String result = tag + " (" + caller + ") :" + msg+'\n';
        printToConsole(level,tag,msg);
        return result;
    }
}
