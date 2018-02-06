package net.lipecki.watson.projection;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProjectionStatus {

    private final boolean stable;
    private final Date statusDate;
    private final Long currentSequenceId;
    private final Long currentMaxSequenceId;
    private final ProjectionState state;

}
