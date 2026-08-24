package com.luckspinquest.dto.spin;

public class SpinResponse {

    private Long spinResultId;
    private Long spinSessionId;
    private Long segmentId;
    private String segmentCode;
    private String segmentName;
    private String segmentType;
    private Long coinReward;
    private Long rewardId;
    private String resultType;

    public Long getSpinResultId() {
        return spinResultId;
    }

    public void setSpinResultId(Long spinResultId) {
        this.spinResultId = spinResultId;
    }

    public Long getSpinSessionId() {
        return spinSessionId;
    }

    public void setSpinSessionId(Long spinSessionId) {
        this.spinSessionId = spinSessionId;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public Long getCoinReward() {
        return coinReward;
    }

    public void setCoinReward(Long coinReward) {
        this.coinReward = coinReward;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
