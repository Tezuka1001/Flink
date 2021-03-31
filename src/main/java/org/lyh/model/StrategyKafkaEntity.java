package org.lyh.model;

/**
 * @author lyh
 * @version 2021-03-30 16:17
 */
public class StrategyKafkaEntity {

    private Long effectStrategyId;

    private String bindedPhone;

    private String uuid;

    private Long creatTime;

    private Integer code;

    public Long getEffectStrategyId() {
        return effectStrategyId;
    }

    public void setEffectStrategyId(Long effectStrategyId) {
        this.effectStrategyId = effectStrategyId;
    }

    public String getBindedPhone() {
        return bindedPhone;
    }

    public void setBindedPhone(String bindedPhone) {
        this.bindedPhone = bindedPhone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
