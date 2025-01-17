package com.datcent.project.module.usertestinfo.service;

import com.datcent.project.module.usertestinfo.domain.Usertestinfo;
import java.util.List;

/**
 * 测试 服务层
 * 
 * @author datcent
 * @date 2018-11-26
 */
public interface IUsertestinfoService 
{
	/**
     * 查询测试信息
     * 
     * @param id 测试ID
     * @return 测试信息
     */
	public Usertestinfo selectUsertestinfoById(Integer id);
	
	/**
     * 查询测试列表
     * 
     * @param usertestinfo 测试信息
     * @return 测试集合
     */
	public List<Usertestinfo> selectUsertestinfoList(Usertestinfo usertestinfo);
	
	/**
     * 新增测试
     * 
     * @param usertestinfo 测试信息
     * @return 结果
     */
	public int insertUsertestinfo(Usertestinfo usertestinfo);
	
	/**
     * 修改测试
     * 
     * @param usertestinfo 测试信息
     * @return 结果
     */
	public int updateUsertestinfo(Usertestinfo usertestinfo);
		
	/**
     * 删除测试信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteUsertestinfoByIds(String ids);
	
}
