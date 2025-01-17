package com.datcent.project.system.user.controller;

import com.datcent.common.utils.StringUtils;
import com.datcent.common.utils.security.ShiroUtils;
import com.datcent.framework.shiro.service.PasswordService;
import com.datcent.project.system.person.domain.Person;
import com.datcent.project.system.person.mapper.PersonMapper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.datcent.common.utils.file.FileUploadUtils;
import com.datcent.framework.aspectj.lang.annotation.Log;
import com.datcent.framework.aspectj.lang.enums.BusinessType;
import com.datcent.framework.config.RuoYiConfig;
import com.datcent.framework.web.controller.BaseController;
import com.datcent.framework.web.domain.AjaxResult;
import com.datcent.framework.web.service.DictService;
import com.datcent.project.system.user.domain.User;
import com.datcent.project.system.user.service.IUserService;

/**
 * 个人信息 业务处理
 * 
 * @author datcent
 */
@Controller
@RequestMapping("/system/user/profile")
public class ProfileController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private String prefix = "system/user/profile";

    @Autowired
    private IUserService userService;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private DictService dict;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap)
    {
        User user = getUser();
        /*user.setSex(dict.getLabel("sys_user_sex", user.getSex()));*/
        mmap.put("user", user);
        mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        mmap.put("postGroup", userService.selectUserPostGroup(Long.parseLong(user.getPersonId())));
        return prefix + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        User user = getUser();
        String encrypt = new Md5Hash(user.getLoginName() + password + user.getSalt()).toHex().toString();
        if (user.getPassword().equals(encrypt))
        {
            return true;
        }
        return false;
    }

    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword)
    {
        User user = ShiroUtils.getUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword))
        {
            user.setPassword(newPassword);
            if (userService.resetUserPwd(user) > 0)
            {
                ShiroUtils.setUser(userService.selectUserById(user.getUserId()));
                return success();
            }
            return error();
        }
        else
        {
            return error("修改密码失败，旧密码错误");
        }
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/edit";
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar/{userId}")
    public String avatar(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/avatar";
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(User user)
    {
        String userName=user.getUserName();
        String Email=user.getEmail();
        String sex=user.getSex();
        String phone=user.getPhonenumber();
        if (userService.updateUserInfo(user) > 0)
        {
            user=userService.selectUserById(user.getUserId());
            Person person=personMapper.selectPersonById(user.getPersonId());
            person.setPersonName(userName);
            person.setEmail(Email);
            person.setMobilePhone(phone);
            person.setSex(sex);
            personMapper.updatePerson(person);
            user=userService.selectUserById(user.getUserId());
            setUser(user);
            return success();
        }
        return error();
    }

    /**
     * 保存头像
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    @ResponseBody
    public AjaxResult updateAvatar(User user, @RequestParam("avatarfile") MultipartFile file)
    {
        try
        {
            if (!file.isEmpty())
            {
                String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
                user.setAvatar(avatar);
                if (userService.updateUserInfo(user) > 0)
                {
                    setUser(userService.selectUserById(user.getUserId()));
                    return success();
                }
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("修改头像失败！", e);
            return error(e.getMessage());
        }
    }
}
