package com.datcent.project.system.config.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datcent.common.utils.poi.ExcelUtil;
import com.datcent.framework.aspectj.lang.annotation.Log;
import com.datcent.framework.aspectj.lang.enums.BusinessType;
import com.datcent.framework.web.controller.BaseController;
import com.datcent.framework.web.domain.AjaxResult;
import com.datcent.framework.web.page.TableDataInfo;
import com.datcent.project.system.config.domain.Config;
import com.datcent.project.system.config.service.IConfigService;

/**
 * 参数配置 信息操作处理
 * 
 * @author datcent
 */
@Controller
@RequestMapping("/system/config")
public class ConfigController extends BaseController
{
    private String prefix = "system/config";

    @Autowired
    private IConfigService configService;

    @RequiresPermissions("system:config:view")
    @GetMapping()
    public String config()
    {
        return prefix + "/config";
    }

    /**
     * 查询参数配置列表
     */
    @RequiresPermissions("system:config:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Config config)
    {
        startPage();
        List<Config> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:config:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Config config)
    {
        List<Config> list = configService.selectConfigList(config);
        ExcelUtil<Config> util = new ExcelUtil<Config>(Config.class);
        return util.exportExcel(list, "config");
    }

    /**
     * 新增参数配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存参数配置
     */
    @RequiresPermissions("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Config config)
    {
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable("configId") Long configId, ModelMap mmap)
    {
        mmap.put("config", configService.selectConfigById(configId));
        return prefix + "/edit";
    }

    /**
     * 修改保存参数配置
     */
    @RequiresPermissions("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Config config)
    {
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(configService.deleteConfigByIds(ids));
    }

    /**
     * 校验参数键名
     */
    @PostMapping("/checkConfigKeyUnique")
    @ResponseBody
    public String checkConfigKeyUnique(Config config)
    {
        return configService.checkConfigKeyUnique(config);
    }

}
