package org.yield4j.tests.annotation;

import static org.yield4j.YieldSupport.yield_return;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//>> [true]
public class PreserveAnnotationOnField {
    @AnAnnotation
    public int i;

    @org.yield4j.Generator public Iterable<Boolean> method() throws Exception {
        yield_return(PreserveAnnotationOnField.class.getField("i").isAnnotationPresent(AnAnnotation.class));
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface AnAnnotation {
}