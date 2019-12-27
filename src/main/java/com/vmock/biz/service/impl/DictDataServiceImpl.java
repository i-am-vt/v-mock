package com.vmock.biz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vmock.biz.entity.DictData;
import com.vmock.biz.mapper.DictDataMapper;
import com.vmock.biz.service.IDictDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author mock
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        return this.list(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType));
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return this.getOne(Wrappers.<DictData>lambdaQuery()
                .eq(DictData::getDictType, dictType)
                .eq(DictData::getDictValue, dictValue)).getDictLabel();
    }
}
