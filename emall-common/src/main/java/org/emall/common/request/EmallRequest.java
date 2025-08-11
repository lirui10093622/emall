package org.emall.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class EmallRequest<T> implements Serializable {
    T data;
}