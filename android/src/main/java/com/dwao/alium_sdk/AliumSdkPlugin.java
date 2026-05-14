package com.dwao.alium_sdk;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dwao.alium.models.Survey;
import com.dwao.alium.models.SurveyParameters;
import com.dwao.alium.survey.Alium;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** AliumSdkPlugin */
public class AliumSdkPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private Activity activity;
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "alium_sdk");
    channel.setMethodCallHandler(this);
    Alium.setShouldResetOnBackground(true);
//    Log.d("channel", "onAttachedToEngine");
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("config")) {
      String url = call.argument("url");

      Alium.config(activity.getApplication(), url);
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("trigger")) {
      String screen = call.argument("screen");
      Map variables = call.argument("variables") == null ? new HashMap<>() : call.argument("variables");
      SurveyParameters surveyParameters = new SurveyParameters(screen, variables);
     Alium.trigger(activity, surveyParameters);
    } else if(call.method.equals("stop")){
      String screen=call.argument("screen");
     if(!screen.isEmpty()) Alium.stop(screen);
    }else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    Log.d("channel", "onDetachedFromEngine");
  }

  @Override
  public void onDetachedFromActivity() {
    // TODO("Not yet implemented");
    Log.d("channel", "onDetachedFromActivity");
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    // TODO("Not yet implemented");
    Log.d("channel", "onReattachedToActivityForConfigChanges");
//    Alium.cleanup();
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    // TODO: your plugin is now attached to an Activity
    this.activity = activityPluginBinding.getActivity();
    Log.d("channel", "onAttachedToActivity");
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // TODO("Not yet implemented");
    Log.d("channel", "onDetachedFromActivityForConfigChanges");
  }
}
