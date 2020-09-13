import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:uhf_rt510_plugin/uhf_rt510_plugin.dart';
import 'package:uhf_rt510_plugin/tag_epc.dart';

void main() => runApp(BarCode());

class BarCode extends StatefulWidget {
  @override
  _BarCodeState createState() => _BarCodeState();
}

class _BarCodeState extends State<BarCode> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
    initUhf();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await UhfRT510Plugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    UhfRT510Plugin.barcodeStatusStream
        .receiveBroadcastStream()
        .listen(updateBarcodes);
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> initUhf() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await UhfRT510Plugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    UhfRT510Plugin.tagsStatusStream.receiveBroadcastStream().listen(updateTags);
    await UhfRT510Plugin.connect;
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  List<TagEpc> _data = [];
  void updateTags(dynamic result) {
    setState(() {
      _data = TagEpc.parseTags(result);
    });
  }

  List<String> _barCodes = [];
  void updateBarcodes(dynamic result) {
    setState(() {
      _barCodes.add(result);
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Barcode PROGAZE'),
        ),
        body: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(3.0),
                  child: Image.asset(
                    'assets/logo.png',
                    width: double.infinity,
                    height: 80,
                    fit: BoxFit.contain,
                  ),
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  RaisedButton(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(18.0),
                      ),
                      color: Colors.blueAccent,
                      child: Text(
                        'Call Scan Single',
                        style: TextStyle(color: Colors.white, fontSize: 36),
                      ),
                      onPressed: () async {
                        await UhfRT510Plugin.scanSingle;
                      }),
                  RaisedButton(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(18.0),
                    ),
                    color: Colors.blueAccent,
                    child: Text(
                      'UHF scan',
                      style: TextStyle(color: Colors.white, fontSize: 36),
                    ),
                    onPressed: () async {
                      await UhfRT510Plugin.start;
                    },
                  ),
                ],
              ),
              RaisedButton(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(18.0),
                  ),
                  color: Colors.blueAccent,
                  child: Text(
                    'Stop uhf',
                    style: TextStyle(color: Colors.white, fontSize: 36),
                  ),
                  onPressed: () async {
                    await UhfRT510Plugin.stop;
                  }),
              RaisedButton(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(18.0),
                  ),
                  color: Colors.blueAccent,
                  child: Text(
                    'Call Clear Data',
                    style: TextStyle(color: Colors.white),
                  ),
                  onPressed: () async {
                    await UhfRT510Plugin.clearDataBarCode;
                    setState(() {
                      _barCodes = [];
                    });
                  }),
              Container(
                width: double.infinity,
                height: 2,
                margin: EdgeInsets.symmetric(vertical: 8),
                color: Colors.blueAccent,
              ),
              ..._barCodes.map((String barcode) => Card(
                    color: Colors.blue.shade50,
                    child: Container(
                      width: 330,
                      alignment: Alignment.center,
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        'Barcode $barcode',
                        style: TextStyle(color: Colors.blue.shade800),
                      ),
                    ),
                  )),
              ..._data.map((TagEpc tag) => Card(
                    color: Colors.blue.shade50,
                    child: Container(
                      width: 330,
                      alignment: Alignment.center,
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        'Tag ${tag.epc} Count:${tag.count}',
                        style: TextStyle(color: Colors.blue.shade800),
                      ),
                    ),
                  )),
            ],
          ),
        ),
      ),
    );
  }
}
