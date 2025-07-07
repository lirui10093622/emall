package org.emall.common.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmallResponse implements Serializable {
    private boolean success;
    private String message;
}