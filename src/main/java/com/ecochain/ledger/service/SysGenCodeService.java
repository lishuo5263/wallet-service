package com.ecochain.ledger.service;

import java.util.List;

import com.ecochain.ledger.model.PageData;

public interface SysGenCodeService {

	/**
	 * 根据groupCode查询代码
	 * @param groupCode
	 */
	public List<PageData> findByGroupCode(String groupCode,String versionNo) throws Exception;
	
	
}
