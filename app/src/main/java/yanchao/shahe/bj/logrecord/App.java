package yanchao.shahe.bj.logrecord;

import android.app.Application;

import java.text.SimpleDateFormat;

import yanchao.shahe.bj.mlog.LogLevel;
import yanchao.shahe.bj.mlog.MLogSettings;
import yanchao.shahe.bj.mlog.WorkMode;

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
        MLogSettings.getInstance().mSdcard(getExternalCacheDir().getAbsolutePath()).mWorkMode(WorkMode.ALL).mLogLevel(LogLevel.WARN).mJsonLog(true).mXmlLog(true)
                .mLogPath("ab").mLogPrefix("@@").fileNameFormat(new SimpleDateFormat("yyyy-MM-dd-HH"));
    }
}
