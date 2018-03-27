package com.glens.controller;

import com.alibaba.fastjson.JSON;
import com.glens.model.Message;
import com.glens.model.UserInfo;
import com.glens.service.UserInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author yk
 * @date 2018/3/11
 */
@RestController
@RequestMapping(value = "/login")
public class LoginController {
    private static Logger logger = Logger.getLogger(LoginController.class.getName());
    @Autowired
    UserInfoService userInfoService;
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public void login(HttpServletResponse response,@RequestParam("userName") String userName,
                      @RequestParam("password") String password) {
        PrintWriter writer=null;
        try {
            response.setContentType("text/json");
            /*设置字符集为'UTF-8'*/
            response.setCharacterEncoding("UTF-8");
            writer=response.getWriter();
        } catch (IOException e) {
            logger.error("[login] Exception：" + e.getMessage());
            e.printStackTrace();
        }
        Message msg=new Message();
        UserInfo user = new UserInfo();
        user.setUserName(userName);
        user.setUserPassword(password);
        List<UserInfo> users = userInfoService.selectByParame(user);
        if (!CollectionUtils.isEmpty(users)){
            msg.setResult(true);
            msg.setErrorMsg(null);
        } else {
            msg.setErrorMsg("登录失败！");
        }
            writer.write(JSON.toJSONString(msg));
    }
}
