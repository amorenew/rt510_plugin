package com.amorenew.rt510.uhf_plugin.helpers;

public interface BarcodeListener {
    void onResult(String id, String barcode, String count);
}
