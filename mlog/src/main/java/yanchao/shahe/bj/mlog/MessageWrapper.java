package yanchao.shahe.bj.mlog;

/**
 * ${TODO} What the class does
 *
 * @author： yanchao
 * @date： 8/25/16-10:24 AM
 * ${TAGS}
 */
public class MessageWrapper {
    private String tag;
    private String msg;

    public MessageWrapper(String v_tag, String v_msg) {
        tag = v_tag;
        msg = v_msg;
    }

    public String getTag() {
        return tag;
    }

    public String getMsg() {
        return msg;
    }
}
