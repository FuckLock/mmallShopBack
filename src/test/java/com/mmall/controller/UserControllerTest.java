package com.mmall.controller;

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

import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        String result  = mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                        .param("username", "admin")
                        .param("password", "adminnew"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.status").value(0))
                        .andReturn().getResponse().getContentAsString();
        logger.info(result);
        logger.info("登陆测试结束");
    }

    @Test
    public void whenLoginFail() throws Exception {
        logger.info("登陆测试开始");
        String result  = mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                        .param("username", "admin")
                        .param("password", "admin"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.status").value(1))
                        .andReturn().getResponse().getContentAsString();
        logger.info(result);
        logger.info("登陆测试结束");
    }

    @Test
    public void whenLogoutSuccess() throws Exception {
        logger.info("退出登录测试开始");
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user/logout.do"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.status").value(0))
                        .andReturn().getResponse().getContentAsString();
        logger.info(result);
        logger.info("退出登录测试结束");
    }

    @Test
    public void whenRegisterSuccess() throws Exception {
        logger.info("注册成功测试开始");
        StringBuffer stringBuffer = new StringBuffer();
        String randId = String.valueOf(new Random().nextInt(999999));
        String username = stringBuffer.append("test").append(randId).toString();
        String password = stringBuffer.append("test").append(randId).toString();
        String email = stringBuffer.append("test").append(randId).append("@qq.com").toString();
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                        .param("username", username)
                        .param("password", password)
                        .param("email", email)
                        .param("phone", "11111111111")
                        .param("question", "问题")
                        .param("answer", "答案"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.status").value(0))
                        .andReturn().getResponse().getContentAsString();
        logger.info(result);
        logger.info("注册成功测试结束");
    }

    @Test
    public void whenRegisterFail() throws Exception {
        logger.info("注册失败测试开始");
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                        .param("username", "admin")
                        .param("password", "adminnew")
                        .param("email", "admin@qq.com")
                        .param("phone", "11111111111")
                        .param("question", "问题")
                        .param("answer", "答案"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.status").value(1))
                        .andReturn().getResponse().getContentAsString();
        logger.info(result);
        logger.info("注册失败测试结束");
    }

    @Test
    public void whenCheckValid() throws Exception {
        logger.info("检查用户验证开始");
        // 检验str参数为空
        String resultFirst = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", " ")
                            .param("type", "username"))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(jsonPath("$.msg").value("参数不可为空"))
                            .andReturn().getResponse().getContentAsString();
        logger.info(resultFirst);

        // 检验type参数为空
        String resultSecond = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", "sdafs")
                            .param("type", " "))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(jsonPath("$.msg").value("参数不可为空"))
                            .andReturn().getResponse().getContentAsString();
        logger.info(resultSecond);

        // 检验type参数传递错误
        String resultThird = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", "testlala")
                            .param("type", "error"))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(jsonPath("$.msg").value("类型参数传递错误"))
                            .andReturn().getResponse().getContentAsString();
        logger.info(resultThird);

        // 检查用户名已经存在
        String resultFour = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", "admin")
                            .param("type", "username"))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(jsonPath("$.msg").value("用户名已经存在"))
                            .andReturn().getResponse().getContentAsString();
        logger.info(resultFour);

        // 检查email已经存在
        String resultFive = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", "admin@qq.com")
                            .param("type", "email"))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(jsonPath("$.msg").value("email已经存在"))
                            .andReturn().getResponse().getContentAsString();
        logger.info(resultFive);

        // 检查成功
        String resultSix = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", "testlala")
                            .param("type", "username"))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(jsonPath("$.msg").value("校验成功"))
                            .andReturn().getResponse().getContentAsString();
        logger.info(resultSix);
        logger.info("检查用户验证结束");
    }

    @Test
    public void whenGetUserInfo() throws Exception {
        logger.info("获取用户信息验证开始");
        // 用户未登录的时候
        String resultFirst = mockMvc.perform(MockMvcRequestBuilders.post("/user/get_user_info.do"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("用户未登录"))
                .andReturn().getResponse().getContentAsString();
        logger.info(resultFirst);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do").cookie()
                .param("username", "admin")
                .param("password", "adminnew"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andReturn().getResponse().getContentAsString();

        String resultSecond = mockMvc.perform(MockMvcRequestBuilders.post("/user/get_user_info.do"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("用户未登录"))
                .andReturn().getResponse().getContentAsString();
        logger.info(resultSecond);

        logger.info("获取用户信息验证结束");
    }

}
