package com.ecochain.ledger.service;



import com.ecochain.ledger.model.PageData;

public interface SysMaxnumService {

	
	/**
	 * 查询最大号码
	 * @param notype
	 */
	public Long findMaxNo(String notype,String versionNo) throws Exception;
	
	/**
	 * 修改最大号码
	 * @param pd
	 * @param versionNo
	 */
	public boolean modifyMaxNo(PageData pd,String versionNo) throws Exception;
	
	
}
