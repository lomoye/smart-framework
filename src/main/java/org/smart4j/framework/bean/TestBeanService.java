package org.smart4j.framework.bean;

import org.smart4j.framework.annotation.Service;

/**
 * Created by lomoye on 2017/5/13.
 * ^_^ 测试注入service
 */
@Service
public class TestBeanService {

    public void testSay() {
        System.out.println("hello, ioc");
    }
}
