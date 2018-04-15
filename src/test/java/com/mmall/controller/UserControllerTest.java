package com.mmall.controller;

import com.mmall.common.RedisShardedPool;
import com.mmall.dao.UserMapper;
import com.mmall.util.CookieUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
                                    "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml" } )
public class UserControllerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserController userController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private MockHttpSession httpSession;

    @Autowired
    private UserMapper userMapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        httpSession = new MockHttpSession();
    }

    @Test
    public void whenLoginSuccess() throws Exception {
        logger.info("登陆测试开始");
        StringBuffer stringBuffer = new StringBuffer();
        String randId = String.valueOf(new Random().nextInt(999999999));
        String username = stringBuffer.append("test").append(randId).toString();
        String password = stringBuffer.append("test").append(randId).toString();
        String email = stringBuffer.append("test").append(randId).append("@qq.com").toString();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username)
                .param("password", password)
                .param("email", email)
                .param("phone", "11111111111")
                .param("question", "问题")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andReturn().getResponse().getContentAsString();

        String result  = mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                        .session(httpSession)
                        .param("username", username)
                        .param("password", password))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(jsonPath("$.status").value(0))
                        .andReturn().getResponse().getContentAsString();
        RedisShardedPoolUtil.del(httpSession.getId());
        logger.info(result);
        logger.info("登陆测试结束");
    }

    @Test
    public void whenLoginFail() throws Exception {
        logger.info("登陆测试开始");
        String result  = mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                        .param("username", "admin")
                        .param("password", "dfsafs"))
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
        String randId = String.valueOf(new Random().nextInt(999999999));
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
        StringBuffer stringBuffer_1 = new StringBuffer();
        String randId_1 = String.valueOf(new Random().nextInt(999999999));
        String username_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String password_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String email_1 = stringBuffer_1.append("test").append(randId_1).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1)
                .param("phone", "11111111111")
                .param("question", " ")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                        .param("username", username_1)
                        .param("password", password_1)
                        .param("email", email_1)
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
        StringBuffer stringBuffer_1 = new StringBuffer();
        String randId_1 = String.valueOf(new Random().nextInt(999999999));
        String username_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String password_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String email_1 = stringBuffer_1.append("test").append(randId_1).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1)
                .param("phone", "11111111111")
                .param("question", " ")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        String resultFour = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", username_1)
                            .param("type", "username"))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(jsonPath("$.msg").value("用户名已经存在"))
                            .andReturn().getResponse().getContentAsString();
        logger.info(resultFour);

        // 检查email已经存在
        String resultFive = mockMvc.perform(MockMvcRequestBuilders.post("/user/check_valid.do")
                            .param("str", email_1)
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
        RedisShardedPoolUtil.del(httpSession.getId());
        String resultFirst = mockMvc.perform(MockMvcRequestBuilders.post("/user/get_user_info.do"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("用户未登录"))
                .andReturn().getResponse().getContentAsString();
        logger.info(resultFirst);

        //用户登录的时候
        StringBuffer stringBuffer_1 = new StringBuffer();
        String randId_1 = String.valueOf(new Random().nextInt(999999999));
        String username_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String password_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String email_1 = stringBuffer_1.append("test").append(randId_1).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1)
                .param("phone", "11111111111")
                .param("question", " ")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                .session(httpSession)
                .param("username", username_1)
                .param("password", password_1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andReturn().getResponse().getContentAsString();


        String resultSecond = mockMvc.perform(MockMvcRequestBuilders.post("/user/get_user_info.do")
                .session(httpSession))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("获取用户信息成功"))
                .andReturn().getResponse().getContentAsString();
        RedisShardedPoolUtil.del(httpSession.getId());
        logger.info(resultSecond);
        logger.info("获取用户信息验证结束");
    }

    @Test
    public void whenForgetGetQuestion() throws Exception {
        logger.info("忘记密码验证开始");
        // 用户不存在的情况下
        mockMvc.perform(MockMvcRequestBuilders.post("/user/forget_get_question.do")
               .param("username", "xiaoshou"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("$.msg").value("用户不存在"))
               .andDo(MockMvcResultHandlers.print());

        /**
         * 用户存在的情况下，但是这个用户的问题不存在
         * 1.我们先创建一个用户，次用户没有属性question,即 user.question = ""
         * 2.进行测试
         * 注意：为了测试每次通过，必须每次都创建一个不通的用户，要不然相同用户会报错
         */

        StringBuffer stringBuffer_1 = new StringBuffer();
        String randId_1 = String.valueOf(new Random().nextInt(999999999));
        String username_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String password_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String email_1 = stringBuffer_1.append("test").append(randId_1).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1)
                .param("phone", "11111111111")
                .param("question", " ")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/forget_get_question.do")
                .param("username", username_1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("找回密码的问题是空的"));

        /**
         * 1. 创建用户 user.quesion="问题"
         * 2.测试
         */

        StringBuffer stringBuffer_2 = new StringBuffer();
        String randId_2 = String.valueOf(new Random().nextInt(999999999));
        String username_2 = stringBuffer_2.append("test").append(randId_2).toString();
        String password_2 = stringBuffer_2.append("test").append(randId_2).toString();
        String email_2 = stringBuffer_2.append("test").append(randId_2).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username_2)
                .param("password", password_2)
                .param("email", email_2)
                .param("phone", "11111111111")
                .param("question", "问题")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/forget_get_question.do")
                .param("username", username_2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        logger.info("忘记密码验证结束");
    }

    @Test
    public void whenResetPassword() throws Exception {
        logger.info("重置密码测试开始");
        // 用户没有登陆的情况下
        RedisShardedPoolUtil.del(httpSession.getId());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/reset_password.do"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("用户未登录"))
                .andDo(print());

        // 用户登陆的情况下
        StringBuffer stringBuffer_1 = new StringBuffer();
        String randId_1 = String.valueOf(new Random().nextInt(999999999));
        String username_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String password_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String email_1 = stringBuffer_1.append("test").append(randId_1).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1)
                .param("phone", "11111111111")
                .param("question", " ")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                .session(httpSession)
                .param("username", username_1)
                .param("password", password_1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/reset_password.do")
                .session(httpSession)
                .param("passwordOld", password_1)
                .param("passwordNew", password_1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("密码更新成功"))
                .andDo(print());

        RedisShardedPoolUtil.del(httpSession.getId());
        logger.info("重置密码测试结束");
    }

    @Test
    public void whenUpdateInformation() throws Exception {
        logger.info("更新个人信息开始");
        // 用户没有登陆的情况下
        RedisShardedPoolUtil.del(httpSession.getId());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/update_information.do"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("用户未登录"));

        // 用户登陆的情况下,当我们根改的email于其他用户相同的情况下
        /**
         * 1.创建两个用户
         * 2.用其中一个用户登陆
         * 3.根改的email于另一个没有登陆的用户email相同
         */
        StringBuffer stringBuffer_1 = new StringBuffer();
        StringBuffer stringBuffer_2 = new StringBuffer();
        String randId_1 = String.valueOf(new Random().nextInt(999999999));
        String randId_2 = String.valueOf(new Random().nextInt(999999999));
        String username_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String username_2 = stringBuffer_2.append("test").append(randId_2).toString();
        String password_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String password_2 = stringBuffer_2.append("test").append(randId_2).toString();
        String email_1 = stringBuffer_1.append("test").append(randId_1).append("@qq.com").toString();
        String email_2 = stringBuffer_2.append("test").append(randId_2).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .session(httpSession)
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1)
                .param("phone", "11111111111")
                .param("question", "问题")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .session(httpSession)
                .param("username", username_2)
                .param("password", password_2)
                .param("email", email_2)
                .param("phone", "11111111111")
                .param("question", "问题")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                .session(httpSession)
                .param("username", username_1)
                .param("password", password_1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/update_information.do")
                .session(httpSession)
                .param("email", email_2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("email已存在,请更换email再尝试更新"));

        // 用户登陆，修改成功
        mockMvc.perform(MockMvcRequestBuilders.post("/user/update_information.do")
                .session(httpSession)
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("更新个人信息成功"));

        RedisShardedPoolUtil.del(httpSession.getId());
        logger.info("重置密码测试结束");
    }

    @Test
    public void whenGetInformation() throws Exception {
        logger.info("获取用户信息验证开始");
        // 用户没有登录的时候
        RedisShardedPoolUtil.del(httpSession.getId());
        String resultFirst = mockMvc.perform(MockMvcRequestBuilders.post("/user/get_information.do"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("用户未登录"))
                .andReturn().getResponse().getContentAsString();
        logger.info(resultFirst);

        //用户登录的时候
        StringBuffer stringBuffer_1 = new StringBuffer();
        String randId_1 = String.valueOf(new Random().nextInt(999999999));
        String username_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String password_1 = stringBuffer_1.append("test").append(randId_1).toString();
        String email_1 = stringBuffer_1.append("test").append(randId_1).append("@qq.com").toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register.do")
                .param("username", username_1)
                .param("password", password_1)
                .param("email", email_1)
                .param("phone", "11111111111")
                .param("question", " ")
                .param("answer", "答案"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login.do")
                .session(httpSession)
                .param("username", username_1)
                .param("password", password_1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andReturn().getResponse().getContentAsString();


        String resultSecond = mockMvc.perform(MockMvcRequestBuilders.post("/user/get_information.do")
                .session(httpSession))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg").value("获取用户成功"))
                .andReturn().getResponse().getContentAsString();
        RedisShardedPoolUtil.del(httpSession.getId());
        logger.info(resultSecond);
        logger.info("获取用户信息验证结束");
    }

}
