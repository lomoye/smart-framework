package org.smart4j.framework.bean;

import org.smart4j.framework.annotation.Service;

/**
 * Created by lomoye on 2017/5/13.
 * ^_^
 */
@Service
public class InjectBeanService {

    public void injectSuccess() {
        System.out.println("auto inject success!!!");
    }
}
