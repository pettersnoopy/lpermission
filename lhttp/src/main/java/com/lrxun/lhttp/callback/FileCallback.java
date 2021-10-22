package com.lrxun.lhttp.callback;

import com.lrxun.lhttp.convert.FileConverter;

import java.io.File;

import okhttp3.Response;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public abstract class FileCallback extends AbsCallback<File> {

    /**
     * 文件转换类
     */
    private FileConverter convert;

    public FileCallback() {
        this(null);
    }

    public FileCallback(String destFileName) {
        this(null, destFileName);
    }

    public FileCallback(String destFileDir, String destFileName) {
        convert = new FileConverter(destFileDir, destFileName);
        convert.setCallback(this);
    }

    @Override
    public File convertResponse(Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }
}
