package com.xesv5.dog;

import com.xesv5.dog.exceptions.AlarmException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AlarmTest {

    @Test
    public void testReport() {
        // ArrayList<Integer> userUids = new ArrayList<>(Arrays.asList(44, 45, 22));

        Alarm alarm = new Alarm.Builder(1, "token")
                // .setAlarmGroups(userUids)
                // .setWeChat(userUids)
                // .setYachWorker(userUids)
                // .setEmail(userUids)
                // .setPhone(userUids)
                // .setSms(userUids)
                // .setDingWorker(userUids)
                // .setDingGroup("122223", "123")
                // .setYachGroup("789", "222")
                .setBaseUri("http://alarm-dog-service.domain.com")
                .build();

        Map<String, Object> content = new HashMap<String, Object>() {{
            put("number", 1);
            put("喜欢", "你");
        }};

        try {
            AlarmResponse resp = alarm.report(content);
            Assert.assertEquals(0, resp.getCode().intValue());
            Assert.assertEquals("success", resp.getMsg());
            System.out.println(resp.getData());

            AlarmResponse resp2 = alarm.report(content, AlarmLevel.ERROR, null);
            Assert.assertEquals(0, resp2.getCode().intValue());
            Assert.assertEquals("success", resp2.getMsg());
            System.out.println(resp2.getData());

        } catch (AlarmException e) {
            System.out.println(e.getMessage());
        }
    }
}
