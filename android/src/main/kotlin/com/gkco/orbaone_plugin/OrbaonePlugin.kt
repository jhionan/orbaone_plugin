package com.gkco.orbaone_plugin

import android.app.Activity
import android.os.Build
import androidx.annotation.NonNull
import com.orbaone.orba_one_capture_sdk_core.OrbaOne
import com.orbaone.orba_one_capture_sdk_core.documentCapture.DocumentCaptureStep
import com.orbaone.orba_one_capture_sdk_core.helpers.DocumentTypes
import com.orbaone.orba_one_capture_sdk_core.helpers.Step
import io.flutter.Log
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
    private lateinit var activity: Activity
    private var oneSdk: OrbaOne? = null

    private val flowStep = arrayOf(Step.INTRO, Step.ID, Step.FACESCAN, Step.COMPLETE)





    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "orbaone_plugin")
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity

        channel.setMethodCallHandler { call, result ->

            when (call.method) {
                "initialSetup" -> {
                    try {
                        val credentials = call.arguments as HashMap<*,*>
                        val apiKey = credentials["apiKey"] as String
                        val applicantId = credentials["applicantId"] as String

                        //todo put exclude configuration on dart layer
                        val docStep = DocumentCaptureStep.Builder()
                                .excludeDocument(arrayOf(DocumentTypes.PASSPORT)).create()//.excludeCountry(arrayOf(CountryCode.JM)).create()


                        oneSdk = OrbaOne.Builder()
                                .setApiKey(apiKey)
                                .setApplicantId(applicantId)
                                .setFlow(flowStep)
                                .setDocumentCapture(docStep)
                                .create()


                    } catch (error: IllegalStateException) {
                        Log.e("SDK Error", error.toString())
                        result.error("500", "${error.message}", "catched")
                    }
                    result.success(true)
                }
                "startIdentification" -> {
                    try {


                    } catch (error: IllegalStateException) {
                        Log.e("SDK Error", error.toString())
                        result.error("500", "${error.message}", "catched")
                    }
                    oneSdk?.startVerification(activity)
                    oneSdk?.onStartVerification(object : OrbaOne.Response {
                        override fun onSuccess() {
                            // Flow started
                            Log.e("Orba", "Flow started")
                        }

                        override fun onFailure(message: String) {
                            // Flow not started
                            Log.e("Orba", "Flow not started")
                        }
                    })
                    oneSdk?.onCompleteVerification(object : OrbaOne.Callback() {
                        override fun execute(key: String) {
                           result.success(true);
                        }
                    })
                    oneSdk?.onCancelVerification(object : OrbaOne.Callback() {
                        override fun execute() {
                            result.success(false)
                            // Flow cancelled by the user.
                            Log.e("Orba", "Flow cancelled by the user.")
                        }
                    })
                }
                "getPlatformVersion" -> {
                    val version = "Android ${Build.VERSION.SDK_INT}"
                    result.success(version);
                }
                else -> {
                    result.notImplemented()
                }
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
