package yanchao.shahe.bj.logrecord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import yanchao.shahe.bj.mlog.MLog;

public class MainActivity extends AppCompatActivity {
    MLog mMLog = MLog.getMLog(MainActivity.class.getName());
    private Button xml, json;
    String xmlstr =
            "<books><book><author>Jack Herrington</author><title>PHP Hacks</title><publisher>O'Reilly</publisher></book><book><author>Jack Herrington</author><title>Podcasting Hacks</title><publisher>O'Reilly</publisher></book><book><author>XML格式化</author><title>脚本之家在线工具</title><publisher>tools.jb51.net</publisher></book></books>";
    String jsonstr = "{\"name\":\"\\u5F20\\u4E09\",\"addtime\":\"2014-01-01\",\"username\":\"abc\",\"id\":5}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xml = (Button) findViewById(R.id.save_xml);
        json = (Button) findViewById(R.id.save_json);

        xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v_view) {
                mMLog.xml(xmlstr);
            }
        });
        json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v_view) {
                mMLog.json(jsonstr);
            }
        });
        mMLog.e("error");
        mMLog.d("ok");
    }
}
