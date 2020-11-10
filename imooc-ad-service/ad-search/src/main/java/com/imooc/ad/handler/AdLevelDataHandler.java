package com.imooc.ad.handler;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adUnit.AdUnitIndex;
import com.imooc.ad.index.adUnit.AdUnitObject;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.index.adplan.AdPlanObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.creativeunit.CreativeUnitObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1. 索引之间存在层级划分，也就是依赖关系的划分
 * 2. 加载全量索引其实是增量索引添加的特殊实现
 */
@Slf4j
public class AdLevelDataHandler {
    public static void handleLevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(planTable.getId(), planTable.getUserId(), planTable.getPlanStatus(),
                planTable.getStartDate(), planTable.getEndDate());
        handleBinlogEvent(DataTable.of(AdPlanIndex.class), planTable.getId(), planObject, type);
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(creativeTable.getAdId(), creativeTable.getName(),
                creativeTable.getType(), creativeTable.getMaterialType(), creativeTable.getHeight(),
                creativeTable.getWidth(), creativeTable.getAuditStatus(), creativeTable.getAdUrl());
        handleBinlogEvent(DataTable.of(CreativeIndex.class), creativeTable.getAdId(), creativeObject, type);
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject planObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (planObject == null) {
            log.error("handleLevel3 found AdPlanObject error: {}", unitTable.getPlanId());
            return;
        }
        AdUnitObject unitObject = new AdUnitObject(unitTable.getUnitId(), unitTable.getUnitStatus(),
                unitTable.getPositionType(), unitTable.getPlanId(), planObject);
        handleBinlogEvent(DataTable.of(AdUnitIndex.class), unitTable.getUnitId(), unitObject, type);
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex cannot support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if (unitObject == null || creativeObject == null) {
            log.error("CreativeUnitIndex index error: {}", JSON.toJSONString(creativeUnitTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getAdId(), creativeUnitTable.getUnitId());
        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(creativeUnitObject.getAdId().toString(), creativeUnitObject.getUnitId().toString()),
                creativeUnitObject, type);
    }

    public static void handleLevel4(AdUnitDistrictTable districtTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("DistrictIndex cannot support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(districtTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error: {}", JSON.toJSONString(districtTable.getUnitId()));
            return;
        }
        String key = CommonUtils.stringConcat(districtTable.getProvince(), districtTable.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(districtTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitDistrictIndex.class), key, value, type);
    }

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("ItIndex cannot support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}", JSON.toJSONString(unitItTable.getUnitId()));
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitItIndex.class), unitItTable.getItTag(), value, type);
    }

    public static void handleLevel4(AdUnitKeywordTable keywordTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("KeywordIndex cannot support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}", JSON.toJSONString(keywordTable.getUnitId()));
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(keywordTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class), keywordTable.getKeyword(), value, type);
    }

    private static <K, V> void handleBinlogEvent(IndexAware<K, V> indexAware, K key, V value, OpType type) {
        switch (type) {
            case ADD:
                indexAware.add(key, value);
                break;
            case UPDATE:
                indexAware.update(key, value);
                break;
            case DELETE:
                indexAware.delete(key, value);
                break;
            default:
                break;
        }
    }
}
