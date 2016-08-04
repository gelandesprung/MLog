package yanchao.shahe.bj.mlog;

/**
 * ${TODO} What the class does
 *
 * @author： yanchao
 * @date： 8/4/16-2:40 PM
 * ${TAGS}
 */
public enum LogLevel {
    JSON(0),XML(1),VERBOSE(2), DEBUG(3), INFO(4), WARN(5), ERROR(6), ASSERT(7);
    private int level;

    LogLevel(int v_level) {
        level = v_level;
    }

    public int getLevel() {
        return level;
    }
}
