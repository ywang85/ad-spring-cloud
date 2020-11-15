package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSlot {
    // 广告位编码
    private String adSlotCode;
    // 流量类型
    private Integer positionType;

    private Integer width;

    private Integer height;

    private List<Integer> type;
    // 广告最低出价
    private Integer minCpm;
}
