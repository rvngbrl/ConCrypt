package com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoAudioConverter;

import java.io.File;

public interface IConvertCallback {
    void onSuccess(File var1);

    void onFailure(Exception var1);
}

