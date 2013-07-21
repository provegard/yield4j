package org.yield4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to use on generator methods. The presence of this annotation
 * instructs the annotation processor to process the method in question. Note
 * that due to how the Pluggable Annotation Processing API is constructed, it is
 * not sufficient to declare the annotation on a generator method in an
 * anonymous inner class - the method in which that class resides must be
 * annotated too.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Generator {

}
