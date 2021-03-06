package com.imooc.ad.search.vo;

import com.imooc.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    // 广告位编码->创意列表
    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creative {
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;
        // 展示监测url
        private List<String> showMonitorUrl = Arrays.asList("www.imooc.com", "www.imooc.com");
        // 点击监测url
        private List<String> clickMonitorUrl = Arrays.asList("www.imooc.com", "www.imooc.com");
    }

    public static Creative convert(CreativeObject creativeObject) {
        Creative creative = new Creative();
        creative.setAdId(creativeObject.getAdId());
        creative.setAdUrl(creativeObject.getAdUrl());
        creative.setWidth(creativeObject.getWidth());
        creative.setHeight(creativeObject.getHeight());
        creative.setType(creativeObject.getType());
        creative.setMaterialType(creativeObject.getMaterialType());
        return creative;
    }
}
