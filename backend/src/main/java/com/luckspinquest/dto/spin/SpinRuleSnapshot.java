package com.luckspinquest.dto.spin;

import java.math.BigDecimal;
import java.util.List;

public class SpinRuleSnapshot {

    private String selectionMode;
    private List<SegmentProbability> segments;

    public String getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(String selectionMode) {
        this.selectionMode = selectionMode;
    }

    public List<SegmentProbability> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentProbability> segments) {
        this.segments = segments;
    }

    public static class SegmentProbability {

        private Long segmentId;
        private BigDecimal probability;

        public Long getSegmentId() {
            return segmentId;
        }

        public void setSegmentId(Long segmentId) {
            this.segmentId = segmentId;
        }

        public BigDecimal getProbability() {
            return probability;
        }

        public void setProbability(BigDecimal probability) {
            this.probability = probability;
        }
    }
}
