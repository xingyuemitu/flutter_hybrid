package cn.missfresh.flutter_hybrid.containers

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.missfresh.flutter_hybrid.FlutterHybridPlugin
import cn.missfresh.flutter_hybrid.Logger
import cn.missfresh.flutter_hybrid.interfaces.IFlutterViewContainer
import cn.missfresh.flutter_hybrid.messaging.Messager.Companion.PARAMS
import cn.missfresh.flutter_hybrid.messaging.Messager.Companion.ROUTE_NAME
import cn.missfresh.flutter_hybrid.view.FHFlutterView

/**
 * Created by sjl
 * on 2019-09-02
 */
class FHFlutterFragment : Fragment(), IFlutterViewContainer {

    private lateinit var mFlutterContent: FlutterViewStub
    private var resumed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FlutterHybridPlugin.instance.getContainerManager().onContainerCreate(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mFlutterContent = FlutterViewStub(activity!!,
                FlutterHybridPlugin.instance.getViewProvider()
                        .createFlutterView(this))
        return mFlutterContent
    }

    override fun onResume() {
        super.onResume()
        if (!resumed) {
            resumed = true
            FlutterHybridPlugin.instance.getContainerManager().onContainerAppear(this)
            mFlutterContent.attachFlutterView(getFHFlutterView())
        }
    }

    override fun onPause() {
        super.onPause()
        if (resumed) {
            resumed = false
            mFlutterContent.snapshot()
            FlutterHybridPlugin.instance.getContainerManager().onContainerDisappear(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mFlutterContent.removeViews()
        FlutterHybridPlugin.instance.getContainerManager().onContainerDestroy(this)
    }

    override fun getContainerName(): String {
        return activity?.intent?.extras?.getString(ROUTE_NAME) ?: ""
    }

    override fun getContainerParams(): Map<String, Any> {
        var params = mapOf<String, Any>()
        activity?.intent?.extras?.getSerializable(PARAMS)?.let {
            params = it as Map<String, Any>
        }
        Logger.d("FHFlutterFragment containerParams:$params")
        return params
    }

    override fun getFHFlutterView(): FHFlutterView {
        return FlutterHybridPlugin.instance.getViewProvider().createFlutterView(this)
    }

    override fun getCurrActivity(): Activity {
        return context as Activity
    }

    override fun onContainerAppear() {
        mFlutterContent.onContainerAppear()
    }

    override fun onContainerDisappear() {
        mFlutterContent.onContainerDisappear()
    }

    override fun destroyContainerView() {
        activity?.finish()
    }

    override fun isFinishing(): Boolean {
        return (context as Activity).isFinishing
    }

    fun setTabTag(tag: String) {
        val args = Bundle()
        args.putString("tag", tag)
        arguments = args
    }

    companion object {
        fun instance(): FHFlutterFragment {
            return FHFlutterFragment()
        }
    }
}