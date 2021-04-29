import 'dart:async';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

class OrbaoneSdk {
  static const MethodChannel _channel = const MethodChannel('orbaone_plugin');
  static bool _isInit = false;

  static Future<String> get platformVersion async {
    final String version =
        await _channel.invokeMethod(_OnbaoneSdkChannel.getPlatformVersion);
    return version;
  }

  static Future<bool> init({@required OrbaoneCredentials credentials}) async {
    _isInit =
        await _channel.invokeMethod<bool>(_OnbaoneSdkChannel.initialSetup, credentials.toMap());
    return _isInit;
  }

  static Future<bool> startIdentification() async {
    final bool result =
        await _channel.invokeMethod(_OnbaoneSdkChannel.startIdentification);

    return result;
  }
}

class _OnbaoneSdkChannel {
  _OnbaoneSdkChannel._();
  static const String getPlatformVersion = 'getPlatformVersion';
  static const String initialSetup = 'initialSetup';
  static const String startIdentification = 'startIdentification';
}

class OrbaoneCredentials {
  OrbaoneCredentials({
    this.apiKey,
    this.applicantId,
  });
  final String apiKey;
  final String applicantId;

  Map<String, String> toMap() =>
      <String, String>{'apiKey': apiKey, 'applicantId': applicantId};
}


