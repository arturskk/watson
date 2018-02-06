package net.lipecki.watson.projection;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProjectionStatus {

    final boolean stable;
    final Date statusDate;
    final Long currentSequenceId;
    final Long currentMaxSequenceId;

}
