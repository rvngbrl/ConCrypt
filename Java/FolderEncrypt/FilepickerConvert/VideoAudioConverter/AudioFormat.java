package com.example.rvn_gbrl.navigationsample.FilepickerConvert.VideoAudioConverter;


public enum AudioFormat {
    MP4,
    MP3,
    MKV,
    MOV,
    AVI,
    M4V,
    FLV,
    MPG,
    WMV,
    AAC,
    OGG,
    M4A,
    WMA,
    FLAC,
    WAV;

    private AudioFormat() {
    }

    public String getFormat() {
        return this.name().toLowerCase();
    }
}