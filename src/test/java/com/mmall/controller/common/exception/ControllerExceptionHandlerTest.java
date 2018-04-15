package com.mmall.controller.common.exception;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
                                    "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml" } )

public class ControllerExceptionHandlerTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenHandleException() throws Exception {
        logger.info("controller层全局异常测试开始");
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                        .param("userna", "admin"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString();
        logger.info(result);
        logger.info("controller层全局异常测试结束");
    }

}
