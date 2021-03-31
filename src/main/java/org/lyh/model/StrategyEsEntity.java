package org.lyh.model;

/**
 * @author lyh
 * @version 2021-03-30 16:29
 */
public class StrategyEsEntity {

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 拦截量
     */
    private Integer interceptNum;

    /**
     * 去重手机号量
     */
    private Integer phoneDistinctNum;

    /**
     * 去重uuid量
     */
    private Integer uuidDistinctNum;

    /**
     * 聚合日期
     */
    private String date;

    /**
     * 聚合小时
     */
    private Integer hour;

    /**
     * 聚合分钟
     */
    private Integer minute;

    /**
     * 10分钟聚合粒度
     */
    private Integer tenMinute;

    /**
     * 聚合时间戳
     */
    private Long dt;

    public StrategyEsEntity() {
    }

    public StrategyEsEntity(Long strategyId, Integer interceptNum, Integer phoneDistinctNum, Integer uuidDistinctNum,
                            String date, Integer hour, Integer minute, Integer tenMinute, Long dt) {
        this.strategyId = strategyId;
        this.interceptNum = interceptNum;
        this.phoneDistinctNum = phoneDistinctNum;
        this.uuidDistinctNum = uuidDistinctNum;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.tenMinute = tenMinute;
        this.dt = dt;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public Integer getInterceptNum() {
        return interceptNum;
    }

    public void setInterceptNum(Integer interceptNum) {
        this.interceptNum = interceptNum;
    }

    public Integer getPhoneDistinctNum() {
        return phoneDistinctNum;
    }

    public void setPhoneDistinctNum(Integer phoneDistinctNum) {
        this.phoneDistinctNum = phoneDistinctNum;
    }

    public Integer getUuidDistinctNum() {
        return uuidDistinctNum;
    }

    public void setUuidDistinctNum(Integer uuidDistinctNum) {
        this.uuidDistinctNum = uuidDistinctNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getTenMinute() {
        return tenMinute;
    }

    public void setTenMinute(Integer tenMinute) {
        this.tenMinute = tenMinute;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }
}
