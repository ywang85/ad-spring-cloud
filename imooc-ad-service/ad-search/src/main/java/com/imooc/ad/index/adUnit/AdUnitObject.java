package com.imooc.ad.index.adUnit;

import com.imooc.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitObject {
    private Long unitId;

    private Integer unitStatus;

    private Integer positionType;

    private Long planId;

    private AdPlanObject adPlanObject;

    void update(AdUnitObject newObject) {
        if (newObject.getUnitId() != null) {
            unitId = newObject.getUnitId();
        }

        if (null != newObject.getUnitStatus()) {
            unitStatus = newObject.getUnitStatus();
        }

        if (null != newObject.getPositionType()) {
            positionType = newObject.getPositionType();
        }

        if (newObject.getPlanId() != null) {
            planId = newObject.getPlanId();
        }

        if (newObject.getAdPlanObject() != null) {
            adPlanObject = newObject.getAdPlanObject();
        }
    }
}
