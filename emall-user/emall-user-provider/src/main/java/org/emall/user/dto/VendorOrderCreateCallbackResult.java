package org.trip.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class VendorOrderCreateCallbackResult implements Serializable {
    String code;
    String message;
}