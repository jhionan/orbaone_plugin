import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:orbaone_plugin/orbaone_plugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('orbaone_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await OrbaonePlugin.platformVersion, '42');
  });
}
