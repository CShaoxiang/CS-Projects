package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.GroupInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController("groupInfoController")
@RequestMapping("/groupInfo")
public class GroupInfoController extends ABaseController{

    @Resource
    private GroupInfoService groupInfoService;

    @RequestMapping("/saveGroup")
    @GlobalInterceptor
    public ResponseVO saveGroup(@Valid @RequestBody GroupInfo groupInfo){


        return getSuccessResponseVO(null);

    }
}
