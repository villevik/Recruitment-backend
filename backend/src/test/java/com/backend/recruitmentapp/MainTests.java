package com.backend.recruitmentapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan({"com.backend.controller"})
class MainTests
    {

    @Test
    void contextLoads()
        {
        }

    }
