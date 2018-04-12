package com.mmall.controlloer;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
                                    "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml" } )
public class UserControllerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenLoginSuccess() throws Exception {
        logger.info("登陆测试开始");
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        logger.info(result);
        logger.info("登陆测试结束");

//        logger.info("登陆测试开始");
//        mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
//                .param("username", "admin")
//                .param("password", "admin"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.status.*").value(0));
//
//        logger.info("登陆测试结束");
    }

}
