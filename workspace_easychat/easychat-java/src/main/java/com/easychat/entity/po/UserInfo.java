package com.easychat.entity.po;


import java.util.Date;
import com.easychat.entity.enums.DateTimePatternEnum;
import com.easychat.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * user_info
 */
public class UserInfo implements Serializable {


	/**
	 * userID
	 */
	private String userId;

	/**
	 * Email
	 */
	private String email;

	/**
	 * Name
	 */
	private String userName;

	/**
	 * 0: join , 1 : agree and join 
	 */
	private Integer joinType;

	/**
	 * 0 : female , 1 : male, 2 : not disclosed
	 */
	private Integer sex;

	/**
	 * password
	 */
	private String password;

	/**
	 * personal introduction
	 */
	private String bio;

	/**
	 * status
	 */
	private Integer status;

	/**
	 * account created time
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * last login time 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
	 * location area
	 */
	private String areaName;

	/**
	 * location code 
	 */
	private String areaCode;

	/**
	 * last off time 
	 */
	private Long lastOffTime;


	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setJoinType(Integer joinType){
		this.joinType = joinType;
	}

	public Integer getJoinType(){
		return this.joinType;
	}

	public void setSex(Integer sex){
		this.sex = sex;
	}

	public Integer getSex(){
		return this.sex;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setBio(String bio){
		this.bio = bio;
	}

	public String getBio(){
		return this.bio;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime(){
		return this.lastLoginTime;
	}

	public void setAreaName(String areaName){
		this.areaName = areaName;
	}

	public String getAreaName(){
		return this.areaName;
	}

	public void setAreaCode(String areaCode){
		this.areaCode = areaCode;
	}

	public String getAreaCode(){
		return this.areaCode;
	}

	public void setLastOffTime(Long lastOffTime){
		this.lastOffTime = lastOffTime;
	}

	public Long getLastOffTime(){
		return this.lastOffTime;
	}

	@Override
	public String toString (){
		return "userID:"+(userId == null ? "空" : userId)+"，Email:"+(email == null ? "空" : email)+"，Name:"+(userName == null ? "空" : userName)+"，0: join , 1 : agree and join :"+(joinType == null ? "空" : joinType)+"，0 : female , 1 : male , 2 : not disclosed :"+(sex == null ? "空" : sex)+"，password:"+(password == null ? "空" : password)+"，personal introduction:"+(bio == null ? "空" : bio)+"，status:"+(status == null ? "空" : status)+"，account create time :"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，last login time :"+(lastLoginTime == null ? "空" : DateUtil.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，location area:"+(areaName == null ? "空" : areaName)+"，location code :"+(areaCode == null ? "空" : areaCode)+"，last off time :"+(lastOffTime == null ? "空" : lastOffTime);
	}
}
