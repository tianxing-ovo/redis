package com.ltx.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Shop extends BasePojo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 商铺名称
     */
    private String name;

    /**
     * 商铺类型
     */
    private Long typeId;

    /**
     * 商铺图片,多个图片以','隔开
     */
    private String images;

    /**
     * 商圈,例如陆家嘴
     */
    private String area;

    /**
     * 地址
     */
    private String address;

    /**
     * 经度
     */
    private Double x;

    /**
     * 纬度
     */
    private Double y;

    /**
     * 均价,取整数
     */
    private Long avgPrice;

    /**
     * 销量
     */
    private Integer sold;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 评分,1~5分,乘10保存,避免小数
     */
    private Integer score;

    /**
     * 营业时间,例如 10:00-22:00
     */
    private String openHours;

    @TableField(exist = false)
    private Double distance;
}
