package org.emall.facade.annotation;

public @interface Auth {
    boolean needLogin() default true;
}