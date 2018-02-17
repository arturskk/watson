package net.lipecki.watson.reatialchain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
public class RetailChain {

    public static final String RETAIL_CHAIN_STREAM = "_retail_chain";

    private String uuid;
    private String name;

}