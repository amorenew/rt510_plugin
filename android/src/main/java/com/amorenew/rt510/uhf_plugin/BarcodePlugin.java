/*package com.amorenew.rt510.uhf_plugin;

import com.amorenew.rt510.uhf_plugin.helpers.BarcodeHelper;
import com.amorenew.rt510.uhf_plugin.helpers.BarcodeListener;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class BarcodePlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {

    private static final String CHANNEL_ScanSingle = "scanSingle";
    private static final String CHANNEL_ScanContinuous = "scanContinuous";
    private static final String CHANNEL_Stop = "stop";
    private static final String CHANNEL_ClearData = "clearData";
    //private static final String CHANNEL_Connect = "connect";
    //private static final String CHANNEL_ConnectedStatus = "ConnectedStatus";
    private static final String CHANNEL_BarcodeStatus = "BarcodeStatus";
    //private static PublishSubject<Boolean> connectedStatus = PublishSubject.create();
    private static PublishSubject<String> barcodeValue = PublishSubject.create();

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
//    public static void registerWith(PluginRegistry.Registrar registrar) {
//        final MethodChannel channel = new MethodChannel(registrar.messenger(), "barcode_plugin");
//        initReadEvent(registrar.messenger());
//        channel.setMethodCallHandler(new BarcodePlugin());
//        BarcodeHelper.getInstance().init(new BarcodeListener() {
//            @Override
//            public void onResult(String id, String barcode, String count) {
//                if (barcode != null)
//                    barcodeValue.onNext(barcode);
//            }
//        });
//    }

    private static void initReadEvent(BinaryMessenger messenger) {
        final EventChannel scannerEventChannel = new EventChannel(messenger, CHANNEL_BarcodeStatus);
        scannerEventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object o, final EventChannel.EventSink eventSink) {
                barcodeValue
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String barcode) {
                        eventSink.success(barcode);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void onCancel(Object o) {

            }
        });
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        final MethodChannel channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "barcode_plugin");
        initReadEvent(flutterPluginBinding.getBinaryMessenger());
        channel.setMethodCallHandler(new BarcodePlugin());
        BarcodeHelper.getInstance().init(new BarcodeListener() {
            @Override
            public void onResult(String id, String barcode, String count) {
                if (barcode != null)
                    barcodeValue.onNext(barcode);
            }
        });
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        handleMethods(call, result);
    }

    private void handleMethods(MethodCall call, MethodChannel.Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            //case CHANNEL_Connect:
            //  result.success(BarcodeHelper.getInstance().init());
            // break;
            case CHANNEL_ScanSingle:
                BarcodeHelper.getInstance().scan(false);
                result.success(true);
                break;
            case CHANNEL_ScanContinuous:
                BarcodeHelper.getInstance().scan(true);
                result.success(true);
                break;
            case CHANNEL_ClearData:
                BarcodeHelper.getInstance().clear();
                result.success(true);
                break;
            case CHANNEL_Stop:
                BarcodeHelper.getInstance().stop();
                result.success(true);
                break;
            default:
                result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    }
}*/
