package com.travest.fingerfx.Entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "finger",
        "finger2",
        "template",
        "template2"
})
public class Finger {

    @JsonProperty("username")
    private String username;
    @JsonProperty("finger")
    private String finger;
    @JsonProperty("finger2")
    private String finger2;
    @JsonProperty("template")
    private String template;
    @JsonProperty("template2")
    private String template2;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("finger")
    public String getFinger() {
        return finger;
    }

    @JsonProperty("finger")
    public void setFinger(String finger) {
        this.finger = finger;
    }

    @JsonProperty("finger2")
    public String getFinger2() {
        return finger2;
    }

    @JsonProperty("finger2")
    public void setFinger2(String finger2) {
        this.finger2 = finger2;
    }

    @JsonProperty("template")
    public String getTemplate() {
        return template;
    }

    @JsonProperty("template")
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty("template2")
    public String getTemplate2() {
        return template2;
    }

    @JsonProperty("template2")
    public void setTemplate2(String template2) {
        this.template2 = template2;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
