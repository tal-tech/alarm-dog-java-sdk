package com.xesv5.dog;

import com.google.gson.annotations.Expose;
import com.xesv5.dog.exceptions.AlarmException;

import java.util.HashMap;

public class AlarmResponse {

    @Expose(deserialize = true)
    private Integer code;

    @Expose(deserialize = true)
    private String msg;

    @Expose(deserialize = true)
    private HashMap<String, Object> data;

    public void validResponse() throws AlarmException {
        switch (code) {
            case 401:
                throw new AlarmException("未授权");
            case 4000:
                throw new AlarmException("参数错误");
            case 4001:
                throw new AlarmException("任务已停止");
            case 4010:
                throw new AlarmException("任务达到速率限制");
            default:
                break;
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }
}
