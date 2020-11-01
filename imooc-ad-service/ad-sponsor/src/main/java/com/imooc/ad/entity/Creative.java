package com.imooc.ad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_creative")
public class Creative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // 图片，视频，文本
    @Column(name = "type", nullable = false)
    private Integer type;

    // 物料类型，比如图片可以是bmp，jpg
    @Column(name = "material_type", nullable = false)
    private Integer materialType;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Column(name = "width", nullable = false)
    private Integer width;

    // 物料大小
    @Column(name = "size", nullable = false)
    private Long size;

    // 持续时长，视频才不为0
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    private Date updateTime;
}
