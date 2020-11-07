package com.imooc.ad.index.creative;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeObject {

    private Long adId;

    private String name;

    private Integer type;

    private Integer materialType;

    private Integer height;

    private Integer width;

    private Integer auditStatus;

    private String adUrl;

    public void update(CreativeObject newObject) {
        if (null != newObject.getAdId()) {
            adId = newObject.getAdId();
        }

        if (null != newObject.getName()) {
            name = newObject.getName();
        }

        if (null != newObject.getType()) {
            type = newObject.getType();
        }

        if (null != newObject.getMaterialType()) {
            materialType = newObject.getMaterialType();
        }

        if (null != newObject.getHeight()) {
            height = newObject.getHeight();
        }

        if (null != newObject.getWidth()) {
            width = newObject.getWidth();
        }

        if (null != newObject.getAuditStatus()) {
            auditStatus = newObject.getAuditStatus();
        }

        if (null != newObject.getAdUrl()) {
            adUrl = newObject.getAdUrl();
        }
    }
}
