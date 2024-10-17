package org.achl.di.entities;

import org.achl.di.enums.SerializableFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @apiNote Annotation that fields from a SerializableObject must have in order for said fields to be serialized
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializableField
{
    //Name of the field on the database
    String name();
    //Type of the field
    SerializableFieldType type();
    //Encode the field in B64
    boolean encode() default false;
}
