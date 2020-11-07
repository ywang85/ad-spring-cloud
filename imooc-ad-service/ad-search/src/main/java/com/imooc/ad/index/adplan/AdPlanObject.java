package com.imooc.ad.index.adplan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanObject {
    private Long planId;

    private Long userId;

    private Integer planStatus;

    private Date startDate;

    private Date endDate;

    public void update(AdPlanObject newObject) {
        if (newObject.getPlanId() != null) {
            planId = newObject.getPlanId();
        }

        if (newObject.getUserId() != null) {
            userId = newObject.getUserId();
        }

        if (newObject.getPlanStatus() != null) {
            planStatus = newObject.getPlanStatus();
        }

        if (newObject.getStartDate() != null) {
            startDate = newObject.getStartDate();
        }

        if (newObject.getEndDate() != null) {
            endDate = newObject.getEndDate();
        }
    }
}
