package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public abstract class AbstractPersistentStateDesc implements PersistentStateDesc {

    protected final String primaryTableName;

    protected final String stateName;

    protected final Class<?> persistentStateType;

    protected String tableName;

    protected String columnName;

    protected boolean persistent;

    protected boolean relationship;

    protected boolean embedded;

    protected PersistentClassDesc persistentStateHolder;

    protected Class collectionType;

    protected Map<String, Column> attribOverrides = new HashMap<String, Column>();

    protected AbstractPersistentStateDesc(String stateName, Class<?> stateType,
            String primaryTableName) {
        if (stateName == null) {
            throw new EmptyRuntimeException("stateName");
        }
        if (stateType == null) {
            throw new EmptyRuntimeException("stateType");
        }
        if (primaryTableName == null) {
            throw new EmptyRuntimeException("primaryTableName");
        }
        this.stateName = stateName;
        this.persistentStateType = stateType;
        this.primaryTableName = primaryTableName;
    }

    protected void introspection(AnnotatedElement target) {
        if (target.isAnnotationPresent(Transient.class)) {
            return;
        }
        persistent = true;

        if (isRelationshipAnnotationPresent(target)) {
            setupRelationship(target);
        } else if (target.isAnnotationPresent(Embedded.class)) {
            setupEmbedded(target);
        } else {
            setupBasic(target);
        }
    }

    protected boolean isToOneRelationship(AnnotatedElement target) {
        return target.isAnnotationPresent(OneToOne.class)
                || target.isAnnotationPresent(ManyToOne.class);
    }

    protected boolean isToManyRelationship(AnnotatedElement target) {
        return target.isAnnotationPresent(OneToMany.class)
                || target.isAnnotationPresent(ManyToMany.class);
    }

    protected boolean isRelationshipAnnotationPresent(AnnotatedElement target) {
        return isToOneRelationship(target) || isToManyRelationship(target);
    }

    protected void setupRelationship(AnnotatedElement target) {
        relationship = true;
        if (isToManyRelationship(target)) {
            return;
        }
        String tableName;
        String columnName;
        Column column = target.getAnnotation(Column.class);
        if (column == null) {
            tableName = primaryTableName;
            columnName = stateName + "_ID";
        } else {
            tableName = StringUtil.isEmpty(column.table()) ? primaryTableName
                    : column.table();
            columnName = StringUtil.isEmpty(column.name()) ? stateName + "_ID" : column
                    .name();
        }
        this.tableName = tableName.toUpperCase();
        this.columnName = columnName.toUpperCase();   
    }

    protected void setupEmbedded(AnnotatedElement target) {
        embedded = true;
        tableName = primaryTableName.toUpperCase();
        AttributeOverride ao = target.getAnnotation(AttributeOverride.class);
        if (ao != null) {
            setupAttributeOverrideds(ao);
        }
        AttributeOverrides aos = target.getAnnotation(AttributeOverrides.class);
        if (aos != null) {
            for (AttributeOverride each : aos.value()) {
                if (each != null) {
                    setupAttributeOverrideds(each);
                }
            }
        }
    }

    protected void setupAttributeOverrideds(AttributeOverride ao) {
        String name = ao.name();
        Column column = ao.column();
        attribOverrides.put(name, column);
    }

    protected void setupBasic(AnnotatedElement target) {
        String tableName;
        String columnName;
        Column column = target.getAnnotation(Column.class);
        if (column == null) {
            tableName = primaryTableName;
            columnName = stateName;
        } else {
            tableName = StringUtil.isEmpty(column.table()) ? primaryTableName
                    : column.table();
            columnName = StringUtil.isEmpty(column.name()) ? stateName : column
                    .name();
        }
        this.tableName = tableName.toUpperCase();
        this.columnName = columnName.toUpperCase();
    }

    protected Class extractCollectionType(Type t) {
        if (t != null && t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type[] genTypes = pt.getActualTypeArguments();
            if (genTypes.length == 1 && genTypes[0] instanceof Class) {
                return (Class) genTypes[0];
            }
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getStateName() {
        return stateName;
    }

    public Class<?> getPersistentStateType() {
        return persistentStateType;
    }

    public boolean isCollection() {
        return collectionType != null;
    }

    public Class<?> getCollectionType() {
        return collectionType;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isRelationship() {
        return relationship;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public PersistentClassDesc createPersistentClass() {
        if (isRelationship()) {
            if (isCollection()) {
                return new EntityClassDesc(getCollectionType());
            }
            return new EntityClassDesc(getPersistentStateType());
        } else if (isEmbedded()) {
            return new AttributeOverridableClassDesc(getPersistentStateType(), getTableName(),
                    isProperty(), Collections.unmodifiableMap(attribOverrides));
        }
        throw new SIllegalStateException("ESSR0501", new Object[]{getPersistentStateType().getName()});
    }

    public abstract Object getValue(Object target);

    public abstract void setValue(Object target, Object value);

    public abstract boolean isProperty();
}
