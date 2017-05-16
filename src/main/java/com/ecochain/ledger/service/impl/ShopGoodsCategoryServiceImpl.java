package com.ecochain.ledger.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecochain.ledger.dao.DaoSupport;
import com.ecochain.ledger.mapper.ShopGoodsCategoryMapper;
import com.ecochain.ledger.model.Page;
import com.ecochain.ledger.model.PageData;
import com.ecochain.ledger.model.ShopGoodsCategory;
import com.ecochain.ledger.service.ShopGoodsCategoryService;

/**
 * Created by LiShuo on 2016/10/25.
 */
@Component("shopGoodsCategoryService")
public class ShopGoodsCategoryServiceImpl implements ShopGoodsCategoryService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private ShopGoodsCategoryMapper shopGoodsCategoryMapper;

    @Override
    public PageData mobileHomeRoom(Page page) throws Exception{
        PageData tpd = new PageData();
        List<PageData> mobileHomeRoom = (List<PageData>)dao.findForList("com.qkl.wlsc.provider.dao.ShopGoodsMapper.listPageMobileHomeRoom", page);
        tpd.put("mobileHomeRoom", mobileHomeRoom);
        tpd.put("page", page);
        return tpd;
    }

    @Override
    public List mobileShowAll(Integer level) {
        List<Map<String, Object>> result =new ArrayList();
        List<Map<String, Object>> result2 =new ArrayList();
        List<Map<String, Object>> allInfo =new ArrayList();
        Map<String,Object> map =new HashMap<>();
        List resultt =new ArrayList();
        result2= this.shopGoodsCategoryMapper.seleceUnionLevelOneNameByLevel(level);
        if(result2 !=null && result2.size()>0){
            for(int i=0;i<result2.size();i++) {
                resultt = this.shopGoodsCategoryMapper.seleceCatId((Integer) result2.get(i).get("cat_id"));
                result2.get(i).put("res", resultt);
                allInfo.add(result2.get(i));
            }
            map.put("allInfo", allInfo);
            result.add(map);
        }else{
            map.put("ErrorInfo","接口参数level 查询不到此分类信息查询失败！");
            result.add(map);
        }
        return result;
    }

    @Override
    public List showAll() {
        List<Map<String,Object>> result =new ArrayList();
        TreeMap<String,Object> map =new TreeMap<String,Object>();
        List<Map<String, Object>> result2 =new ArrayList();
        List<Map<String,Object>> result3 =new ArrayList();
        List resultt =new ArrayList();
        //List<ShopGoodsCategory> list=this.shopGoodsCategoryMapper.showAll();
        //map.put("levelOneName",this.shopGoodsCategoryMapper.seleceUnionLevelOneName());
        //result3 =this.shopGoodsCategoryMapper.seleceLevelTwoNameInfo();
        result2 =this.shopGoodsCategoryMapper.seleceLevelOneInfo();
        List<Map<String, Object>> allinfo1 =new ArrayList();
        List<Map<String, Object>> allinfo2 =new ArrayList();
        List<Map<String, Object>> allinfo3 =new ArrayList();
        List<Map<String, Object>> allinfo4 =new ArrayList();
        List<Map<String, Object>> allinfo5 =new ArrayList();
        List<Map<String, Object>> allinfo6 =new ArrayList();
        List<Map<String, Object>> allinfo7 =new ArrayList();
        List<Map<String, Object>> allinfo8 =new ArrayList();
        List<Map<String, Object>> allinfo9 =new ArrayList();
        List<Map<String, Object>> allinfo10 =new ArrayList();
        List<Map<String, Object>> allinfo11 =new ArrayList();
        /*resultt= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
        result2.get(i).put("res",resultt);
        allinfo1.add(result2.get(i));*/
        for(int i=0;i<result2.size();i++){
            if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==1){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo1.add(result2.get(i));
                }
            }else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==2){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo2.add(result2.get(i));
                }
            }else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==3){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo3.add(result2.get(i));
                }
            }
            else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==4){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo4.add(result2.get(i));
                }
            }
            else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==5){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo5.add(result2.get(i));
                }
            }
            else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==6){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo6.add(result2.get(i));
                }
            }
            else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==7){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo7.add(result2.get(i));
                }
            }
            else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==8){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo8.add(result2.get(i));
                }
            }
            else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==9){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo9.add(result2.get(i));
                }
            }
            else if(result2.get(i)!=null&&(Integer)result2.get(i).get("is_level")==10){
                result3= this.shopGoodsCategoryMapper.seleceCatId((Integer)result2.get(i).get("cat_id"));
                for(int k=0;k<result3.size();k++){
                    int count=this.shopGoodsCategoryMapper.selectGoodsCount((Integer)result3.get(k).get("cat_id"));
                    if(count>0){
                        allinfo11.add(result3.get(k));
                    }
                }
                if(allinfo11.size() > 0){
                    result2.get(i).put("res",allinfo11);
                    allinfo11=new ArrayList<>();
                    allinfo10.add(result2.get(i));
                }
            }
        }
        map.put("allinfo1",allinfo1);
        map.put("allinfo2",allinfo2);
        map.put("allinfo3",allinfo3);
        map.put("allinfo4",allinfo4);
        map.put("allinfo5",allinfo5);
        map.put("allinfo6",allinfo6);
        map.put("allinfo7",allinfo7);
        map.put("allinfo8",allinfo8);
        map.put("allinfo9",allinfo9);
        map.put("allinfo10",allinfo10);
        result.add(map);
        return result;
    }

    public StringBuffer getMenuByLayer(List<ShopGoodsCategory> list, StringBuffer buffer) throws Exception{
        List list1 = new ArrayList<>(); // 一级菜单
        List list2 = new ArrayList<>(); // 二级菜单
        //List list3 = new ArrayList<>(); // 三级菜单
        for (ShopGoodsCategory shopGoodsCategory : list) {
            if("0".equals(shopGoodsCategory.getParentId())){
                list1.add(shopGoodsCategory);
            }else if(shopGoodsCategory.getIsLevel().intValue() == 2){
                list2.add(shopGoodsCategory);
            }/*else if(shopGoodsCategory.getIsLevel().intValue() == 3){
                list3.add(shopGoodsCategory);
            }*/
        }
        // 遍历一级
        for (Iterator iterator = list1.iterator(); iterator.hasNext();){
            ShopGoodsCategory privs1 = (ShopGoodsCategory) iterator.next();
            // 一级菜单开始
            // 遍历二级
            for (Iterator iterator2 = list2.iterator(); iterator2.hasNext();) {
                ShopGoodsCategory privs2 = (ShopGoodsCategory) iterator2.next();
                if(privs2.getParentId().trim().equals(privs1.getCatId().toString())){
                    // 二级菜单开始
                    buffer.append("{\"catName\":\""+privs2.getCatName()+"\""+",\"catId\":"+privs2.getCatId()+",\"parentId\":"+privs2.getParentId()+"},");

                    // 遍历三级
                    /*for (Iterator iterator3 = list3.iterator(); iterator3.hasNext();) {
                        ShopGoodsCategory privs3 = (ShopGoodsCategory) iterator3.next();
                        if(privs3.getParentId().trim().equals(privs2.getCatId().toString())){
                            // 二级菜单下三级菜单
                            buffer.append("{\"catName\":\""+privs3.getCatName()+"\""+",\"catId\":"+privs3.getCatId()+",\"parentId\":"+privs3.getParentId()+"},");
                        }
                    }*/
                    // 二级菜单结束
                    buffer.append("}");
                }
            }
            // 一级菜单结束
            buffer.append("]");
        }
        return buffer;
    }
}

