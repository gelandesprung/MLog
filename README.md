# MLog
格式化日志，包括json和xml，按日期保存到sd卡上
## 使用方法
初始化
``` java
MLogSettings.getInstance().mWorkMode(WorkMode.ALL).mLogLevel(LogLevel.WARN).mJsonLog(true).mXmlLog(true)
                .mLogPath("ab").mLogPrefix("@@").fileNameFormat(new SimpleDateFormat("yyyy-MM-dd-HH"));
```
* `mLogPrefix`指定日志文件的前缀，日志文件的格式是`前缀+日期.log`,
* `mLogLevel`指定日志输出的等级
* `mJsonLog`为true时输出json日志，否则不输出
* `mXmlLog`为true时输出xml日志，否则不输出
* `mLogPath`是日志文件保存的路径
* `fileNameFormat`格式化时候作为日志文件名的一部分，所以它能决定日志输出的精确度，格式化为`年-月-日-时-分`的效果就是第分钟换一个文件写日志
* `dateFormat`是在日志中显示的日期格式
使用方法也不太复杂，在使用类的开始处构造一个`MLog`对象，之后就可以像`Log.xxx`一样调用了。`MLog`的参数是一个tag，建议使用类名，因为我在格式化日志时没有查找类名，直接用它当类名了。
``` java
MLog mMLog = MLog.getMLog(MainActivity.class.getName());
...

mMLog.xml(xmlstr);
...
mMLog.e("error");
mMLog.d("ok");
,,,
```
