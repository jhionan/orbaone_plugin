package com.gkco.orbaone_plugin

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import com.orbaone.orba_one_capture_sdk_core.OrbaOne
import com.orbaone.orba_one_capture_sdk_core.helpers.Step
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodChannel

/** OrbaonePlugin */
class OrbaonePlugin : FlutterPlugin, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var mContext: Context
    private lateinit var activity: Activity


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "orbaone_plugin")
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity

        channel.setMethodCallHandler { call, result ->
            if (call.method == "getPlatformVersion") {
                val flowStep: Array<Step> = arrayOf<Step>(Step.INTRO, Step.ID, Step.FACESCAN, Step.COMPLETE)
                val oneSdk = OrbaOne.Builder()
                        .setApiKey("key")
                        .setApplicantId("applicant")
                        .setFlow(flowStep)
                        .create()

                oneSdk.onStartVerification(object : OrbaOne.Response {
                    override fun onSuccess() {
                        // Flow started
                    }

                    override fun onFailure(message: String) {
                        // Flow not started
                    }
                })

                oneSdk.onCompleteVerification(object : OrbaOne.Callback() {
                    override fun execute(key: String) {
                        // Flow completed successfully. The applicant id is also returned as a parameter.
                    }
                })

                oneSdk.onCancelVerification(object : OrbaOne.Callback() {
                    override fun execute() {
                        // Flow cancelled by the user.
                    }
                })
                oneSdk.startVerification(activity)





                //result.success("Android ${android.os.Build.VERSION.RELEASE}")
            } else {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }
}
