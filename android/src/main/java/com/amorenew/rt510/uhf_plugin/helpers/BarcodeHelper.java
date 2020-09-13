package com.amorenew.rt510.uhf_plugin.helpers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class BarcodeHelper {

    private static BarcodeHelper instance;
    private BarcodeListener barcodeListener;

    private cn.pda.serialport.scan.ScanThread scanThread;
    private List<Barcode> listBarcode = new ArrayList<>();
    private List<Map<String, String>> listMap;
    private Timer scanTimer = null;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == cn.pda.serialport.scan.ScanThread.SCAN) {
                String data = msg.getData().getString("data");
                Log.e("Barcode", "data = " + data);
                sortAndadd(listBarcode, data);
                addListView();
            }
        }

        ;
    };

    private BarcodeHelper() {
    }

    public static BarcodeHelper getInstance() {
        if (instance == null)
            instance = new BarcodeHelper();
        return instance;
    }

    public boolean init(BarcodeListener barcodeListener) {
        this.barcodeListener = barcodeListener;
        scanTimer = new Timer();
        try {
            scanThread = new cn.pda.serialport.scan.ScanThread(mHandler);
        } catch (Exception e) {
            return false;
        }
        scanThread.start();
        return true;
    }

    public void scan(boolean isContinuous) {
        if (!isContinuous) {
            scanThread.scan();
            scanTimer.cancel();
        } else {
            scanTimer.cancel();
            scanTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (scanThread != null) {
                        scanThread.scan();
                    }

                }
            }, 100, 200);
        }
    }

    public void clear() {
        listBarcode.removeAll(listBarcode);
    }

    public void stop() {
        if (scanThread != null) {
            scanTimer.cancel();
            scanThread.interrupt();
            scanThread.close();
        }
    }

    private void addListView() {
        listMap = new ArrayList<Map<String, String>>();
        int id = 1;
        for (Barcode barcode : listBarcode) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", id + "");
            map.put("barcode", barcode.getBarcode());
            map.put("count", barcode.getCount() + "");
            listMap.add(map);
            barcodeListener.onResult(id + "",barcode.getBarcode(),barcode.getCount() + "");

        }
    }

    private List<Barcode> sortAndadd(List<Barcode> list, String barcode) {
        Barcode goods = new Barcode();
        goods.setBarcode(barcode);
        int temp = 1;
        if (list == null || list.size() == 0) {
            goods.setCount(temp);
            list.add(goods);
            return list;
        }

        for (int i = 0; i < list.size(); i++) {
            if (barcode.equals(list.get(i).getBarcode())) {
                temp = list.get(i).getCount() + temp;
                goods.setCount(temp);
                for (int j = i; j > 0; j--) {
                    list.set(j, list.get(j - 1));
                }
                list.set(0, goods);
                return list;
            }
        }
        //
        Barcode lastgoods = list.get(list.size() - 1);
        for (int j = list.size() - 1; j >= 0; j--) {
            if (j == 0) {
                goods.setCount(temp);
                list.set(j, goods);
            } else {
                list.set(j, list.get(j - 1));
            }

        }
        list.add(lastgoods);
        return list;
    }

}
