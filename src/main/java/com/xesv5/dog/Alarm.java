package com.xesv5.dog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.xesv5.dog.exceptions.AlarmException;
import com.xesv5.dog.http.DogClient;
import com.xesv5.dog.utils.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Alarm {

    private String postJson;

    private String respJson;

    private final DogClient dogClient = new DogClient();

    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private final String token;

    private final String baseUri;

    @Expose(serialize = true)
    private HashMap<String, Object> channels;

    @Expose(serialize = true)
    private ArrayList<HashMap<String, String>> yachGroups;

    @Expose(serialize = true)
    private ArrayList<HashMap<String, String>> dingGroups;

    @Expose(serialize = true)
    private final Integer taskid;

    @Expose(serialize = true)
    private String timestamp;

    @Expose(serialize = true)
    private String sign;

    @Expose(serialize = true)
    private String notice_time;

    @Expose(serialize = true)
    private HashMap<String, Object> receiver;

    @Expose(serialize = true)
    private Map<String, Object> ctn;

    @Expose(serialize = true)
    private Integer level;

    Alarm(Builder builder) {
        this.taskid = builder.taskid;
        this.token = builder.token;
        this.baseUri = builder.baseUri;
        this.receiver = builder.receiver;
        this.channels = builder.channels;
        this.yachGroups = builder.yachGroup;
        this.dingGroups = builder.dingGroup;
    }

    public static class Builder {
        // 必选参数
        private Integer taskid;
        private String token;
        // 可选参数
        private HashMap<String, Object> receiver = new HashMap<>();
        private HashMap<String, Object> channels = new HashMap<>();
        private ArrayList<HashMap<String, String>> dingGroup = new ArrayList<>();
        private ArrayList<HashMap<String, String>> yachGroup = new ArrayList<>();
        private String baseUri = "https://alarm-dog-service.domain.com";

        public Builder(Integer taskid, String token) {
            this.taskid = taskid;
            this.token = token;
        }

        public Alarm build() {
            return new Alarm(this);
        }


        public Builder setBaseUri(String uri) {
            this.baseUri = uri;
            return this;
        }

        public Builder setAlarmGroups(ArrayList<Integer> list) {
            this.receiver.put("alarmgroup", list);
            return this;
        }

        public Builder setDingGroup(String token, String secret) {
            HashMap<String, String> map = new HashMap<String, String>() {{
                put("webhook", token);
                put("secret", secret);
            }};
            dingGroup.add(map);
            this.channels.put("dinggroup", dingGroup);
            this.receiver.put("channels", channels);
            return this;
        }

        public Builder setDingWorker(ArrayList<Integer> dingWorker) {
            this.channels.put("dingworker", dingWorker);
            this.receiver.put("channels", channels);
            return this;
        }

        public Builder setYachGroup(String token, String secret) {
            HashMap<String, String> map = new HashMap<String, String>() {{
                put("webhook", token);
                put("secret", secret);
            }};
            yachGroup.add(map);
            this.channels.put("yachgroup", yachGroup);
            this.receiver.put("channels", channels);
            return this;
        }

        public Builder setYachWorker(ArrayList<Integer> yachWorker) {
            this.channels.put("yachworker", yachWorker);
            this.receiver.put("channels", channels);
            return this;
        }

        public Builder setEmail(ArrayList<Integer> email) {
            this.channels.put("email", email);
            this.receiver.put("channels", channels);
            return this;
        }

        public Builder setSms(ArrayList<Integer> sms) {
            this.channels.put("sms", sms);
            this.receiver.put("channels", channels);
            return this;
        }

        public Builder setWeChat(ArrayList<Integer> weChat) {
            this.channels.put("wechat", weChat);
            this.receiver.put("channels", channels);
            return this;
        }

        public Builder setPhone(ArrayList<Integer> phone) {
            this.channels.put("phone", phone);
            this.receiver.put("channels", channels);
            return this;
        }
    }

    /**
     * @param content          : 告警内容kv对象（必填）
     * @param alarmLevellevel  : 告警级别（必填）
     * @param assignNoticeTime : 指定通知时间，String Unix时间戳 （10位，单位秒，非必填可为null）
     * @return AlarmResponse
     */
    public AlarmResponse report(Map<String, Object> content, AlarmLevel alarmLevellevel, String assignNoticeTime) throws AlarmException {
        ctn = content;
        timestamp = Common.getCurrentTimeStamp();
        level = alarmLevellevel.ordinal();
        sign = Common.getSign(taskid, timestamp, token);
        notice_time = assignNoticeTime;
        postJson = gson.toJson(this);
        respJson = dogClient.post(baseUri + "/alarm/report", postJson);

        AlarmResponse alarmResponse = new Gson().fromJson(respJson, AlarmResponse.class);
        alarmResponse.validResponse();

        return alarmResponse;
    }

    /**
     * @param content : 告警内容kv对象（必填）
     * @return AlarmResponse
     */
    public AlarmResponse report(Map<String, Object> content) throws AlarmException {
        ctn = content;
        level = AlarmLevel.NOTICE.ordinal();
        timestamp = Common.getCurrentTimeStamp();
        sign = Common.getSign(taskid, timestamp, token);
        postJson = gson.toJson(this);
        respJson = dogClient.post(baseUri + "/alarm/report", postJson);

        AlarmResponse alarmResponse = new Gson().fromJson(respJson, AlarmResponse.class);
        alarmResponse.validResponse();

        return alarmResponse;
    }


    /**
     * @return AlarmResponse
     */
    public AlarmResponse test() throws AlarmException {
        timestamp = Common.getCurrentTimeStamp();
        sign = Common.getSign(taskid, timestamp, token);
        postJson = gson.toJson(this);
        respJson = dogClient.post(baseUri + "/alarm/test", postJson);

        AlarmResponse alarmResponse = new Gson().fromJson(respJson, AlarmResponse.class);
        alarmResponse.validResponse();

        return alarmResponse;
    }
}
