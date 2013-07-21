package org.yield4j.tests.annotation;
import static org.yield4j.YieldSupport.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//>> [true]
public class PreserveAnnotationsOnMethod {
    @AnAnnotation
    @org.yield4j.Generator public Iterable<Boolean> method() throws Exception {
        yield_return(PreserveAnnotationsOnMethod.class.getMethod("method").isAnnotationPresent(AnAnnotation.class));
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface AnAnnotation {
}