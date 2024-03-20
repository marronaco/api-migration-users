package com.eviden.migration.model.magento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagentoUsuario {
    //atributos de para insertar usuario
    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("password")
    private String password;


    //Inner clases de los atributos de tipo objeto
    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExtensionAttributes {

        @JsonProperty("isSubscribed")
        private boolean isSubscribed;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Addresses {

        @JsonProperty("id")
        private int id;

        @JsonProperty("customer_id")
        private int customerId;

        @JsonProperty("region")
        private Region region;

        @JsonProperty("region_id")
        private int regionId;

        @JsonProperty("country_id")
        private String countryId;

        @JsonProperty("street")
        private List<String> street;

        @JsonProperty("telephone")
        private String telephone;

        @JsonProperty("postcode")
        private String postcode;

        @JsonProperty("city")
        private String city;

        @JsonProperty("firstname")
        private String firstname;

        @JsonProperty("lastname")
        private String lastname;

        @JsonProperty("default_shipping")
        private boolean defaultShipping;

        @JsonProperty("default_billing")
        private boolean defaultBilling;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Region {

        @JsonProperty("region_code")
        private String regionCode;

        @JsonProperty("region")
        private String region;

        @JsonProperty("region_id")
        private int regionId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Customer {

        @JsonProperty("group_id")
        private int groupId;

        @JsonProperty("email")
        private String email;

        @JsonProperty("firstname")
        private String firstname;

        @JsonProperty("lastname")
        private String lastname;

        @JsonProperty("gender")
        private int gender; //revisar cuales son los codigos para el tipo de gender en magento

        @JsonProperty("storeId")
        private int storeId;

        @JsonProperty("websiteId")
        private int websiteId;

        @JsonProperty("addresses")
        private List<Addresses> addresses;

        @JsonProperty("disableAutoGroupChange")
        private int disableAutoGroupChange;

        @JsonProperty("extensionAttributes")
        private ExtensionAttributes extensionAttributes;
    }
}
