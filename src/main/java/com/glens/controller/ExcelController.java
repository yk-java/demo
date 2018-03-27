package com.glens.controller;

import com.glens.excel.ExcelUtils;
import com.glens.model.UserInfo;
import com.glens.service.UserInfoService;
import com.glens.utils.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by yk on 2018/1/20.
 * @author yk
 */
@RequestMapping(value = "/excel")
@Controller
public class ExcelController {
    private static Logger logger = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    private UserInfoService userInfoService;
    /**
     * 用户详细信息导出
     */
    @RequestMapping(value = "/downUser",method = RequestMethod.GET)
    public void listUser(HttpServletResponse response){
        List<UserInfo> userList = userInfoService.findAll();
        String fileName = "用户信息_" + DateFormatter.stringDate(new Date());
        ExcelUtils.writeExcel(response,fileName,userList,UserInfo.class);
       logger.info("导出成功");
    }
}
