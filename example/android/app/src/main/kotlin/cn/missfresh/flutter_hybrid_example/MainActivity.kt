package cn.missfresh.flutter_hybrid_example

import android.os.Bundle
import android.view.View
import cn.missfresh.flutter_hybrid.FlutterHybridPlugin
import cn.missfresh.flutter_hybrid.Logger
import cn.missfresh.flutter_hybrid_example.util.RouterUtil

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant
import kotlinx.android.synthetic.main.main_activity.*
import java.lang.ref.WeakReference

class MainActivity : FlutterActivity(), View.OnClickListener {

    companion object {
        var sRef: WeakReference<MainActivity>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sRef = WeakReference(this)

        setContentView(R.layout.main_activity)
        initView()
        GeneratedPluginRegistrant.registerWith(this)
    }

    private fun initView() {
        tv_title.text = "MainActivity"
        tv_open_native_activity.setOnClickListener(this)
        tv_open_flutter_activity.setOnClickListener(this)
        tv_open_flutter_fragment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_open_native_activity -> RouterUtil.openPageByUrl(this, RouterUtil.NATIVE_ACTIVITY_URL)
            R.id.tv_open_flutter_activity -> RouterUtil.openPageByUrl(this, RouterUtil.FLUTTER_ACTIVITY_URL)
            R.id.tv_open_flutter_fragment -> RouterUtil.openPageByUrl(this, RouterUtil.FLUTTER_FRAGMENT_ACTIVITY_URL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FlutterHybridPlugin.instance.destroy()
        sRef?.clear()
        sRef = null
    }
}
