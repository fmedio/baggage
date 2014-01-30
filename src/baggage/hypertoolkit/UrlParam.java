package baggage.hypertoolkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* Created by fmedio on 1/29/14.
*/
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@interface UrlParam {}
