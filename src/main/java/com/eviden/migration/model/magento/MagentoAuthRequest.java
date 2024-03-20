package com.eviden.migration.model.magento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagentoAuthRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
