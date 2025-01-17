package com.datcent.project.module.massesReport.service;

import com.datcent.project.module.massesReport.domain.MassesReport;
import java.util.List;

/**
 * 群众举报情况 服务层
 * 
 * @author datcent
 * @date 2019-04-01
 */
public interface IMassesReportService 
{
	/**
     * 查询群众举报情况信息
     * 
     * @param xfjbqkId 群众举报情况ID
     * @return 群众举报情况信息
     */
	public MassesReport selectMassesReportById(String xfjbqkId);
	
	/**
     * 查询群众举报情况列表
     * 
     * @param massesReport 群众举报情况信息
     * @return 群众举报情况集合
     */
	public List<MassesReport> selectMassesReportList(MassesReport massesReport);
	
	/**
     * 新增群众举报情况
     * 
     * @param massesReport 群众举报情况信息
     * @return 结果
     */
	public int insertMassesReport(MassesReport massesReport);
	
	/**
     * 修改群众举报情况
     * 
     * @param massesReport 群众举报情况信息
     * @return 结果
     */
	public int updateMassesReport(MassesReport massesReport);
		
	/**
     * 删除群众举报情况信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMassesReportByIds(String ids);
	
}
