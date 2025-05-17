package com.chakir.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
//This implies that SimplyAutoWire can only be applied to Fields
@Retention(RetentionPolicy.RUNTIME)
//This implies that SimplyAutoWire has Runtime Visibility
public @interface SimplyAutoWire {
    //Since it has no methods it simply serves as a marker
}
