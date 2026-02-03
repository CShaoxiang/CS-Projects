package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.GroupInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController("groupInfoController")
@RequestMapping("/group")
public class GroupInfoController extends ABaseController{

    @Resource
    private GroupInfoService groupInfoService;

    @RequestMapping("/saveGroup")
    @GlobalInterceptor
    public ResponseVO saveGroup(@Valid @RequestBody GroupInfo groupInfo,
                                HttpServletRequest request ,MultipartFile avatarFile,
                                MultipartFile avatarCover){

        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        groupInfo.setGroupId(groupInfo.getGroupId());
        groupInfo.setGroupOwnerId(tokenUserInfoDto.getUserId());
        groupInfo.setGroupName(groupInfo.getGroupName());
        groupInfo.setGroupNotice(groupInfo.getGroupNotice());
        groupInfo.setJoinType(groupInfo.getJoinType());
        this.groupInfoService.saveGroup(groupInfo,avatarFile,avatarCover);
        return getSuccessResponseVO(null);

    }
}
