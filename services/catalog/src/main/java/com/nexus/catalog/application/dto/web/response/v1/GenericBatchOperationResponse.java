package com.nexus.catalog.application.dto.web.response.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.nexus.catalog.application.dto.web.OutboundView;

public record GenericBatchOperationResponse(

        @JsonView(OutboundView.Brief.class)
        BatchOperationType operation,

        @JsonView(OutboundView.Brief.class)
        Integer numberOfRowsAffected,

        @JsonView(OutboundView.Brief.class)
        String message

) {
}
