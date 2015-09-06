package com.baidu.ub.msoa.example.integration.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by pippo on 15/8/22.
 */
@Component
@Lazy(false)
public class Echo {

    @PostConstruct
    public void init() {
        System.out.println("#####echo");
    }

}
