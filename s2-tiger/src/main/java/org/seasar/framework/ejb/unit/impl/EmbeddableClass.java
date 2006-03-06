package org.seasar.framework.ejb.unit.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.seasar.framework.ejb.unit.AnnotationNotFoundRuntimeException;
import org.seasar.framework.ejb.unit.PersistentClass;
import org.seasar.framework.ejb.unit.PersistentState;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 *
 */
public class EmbeddableClass implements PersistentClass {

    private String name;

    private Class<?> embeddableClass;

    private boolean propertyAccessed;

    private List<String> tableNames = new ArrayList<String>();

    private PersistentStateHolder stateHolder;

    private Map<String, Column> columnsByName;

    public EmbeddableClass(Class<?> embeddableClass, String tableName,
            boolean propertyAccessed, Map<String, Column> columnsByName) {

        this.embeddableClass = embeddableClass;
        if (!embeddableClass.isAnnotationPresent(Embeddable.class)) {
            throw new AnnotationNotFoundRuntimeException("@Embeddable",
                    embeddableClass.getName());
        }
        if (columnsByName == null) {
            throw new EmptyRuntimeException("overrideColumnsByName");
        }
        this.name = ClassUtil.getShortClassName(embeddableClass);
        this.propertyAccessed = propertyAccessed;
        this.tableNames.add(tableName);
        this.stateHolder = new PersistentStateHolder(embeddableClass,
                tableName, propertyAccessed);
        this.columnsByName = columnsByName;
    }

    public PersistentState getAttributeOverridedState(PersistentState original,
            Column column) {

        AttributeOverridedState aos = new AttributeOverridedState(original);
        aos.setColumnName(column.name().toUpperCase());
        aos.setTableName(column.table().toUpperCase());
        return aos;
    }

    public String getName() {
        return name;
    }

    public Class<?> getPersistentClassType() {
        return embeddableClass;
    }

    public PersistentState getPersistentState(int index) {
        PersistentState original = stateHolder.getPersistentState(index);
        if (columnsByName.containsKey(original.getStateName())) {
            Column column = columnsByName.get(original.getStateName());
            return getAttributeOverridedState(original, column);
        }
        return original;
    }

    public PersistentState getPersistentState(String stateName) {
        for (Map.Entry<String, Column> e : columnsByName.entrySet()) {
            Column column = e.getValue();
            if (column.name().equals(stateName)) {
                PersistentState original = stateHolder.getPersistentState(e
                        .getKey());
                return getAttributeOverridedState(original, column);
            }
        }
        return stateHolder.getPersistentState(stateName);
    }

    public int getPersistentStateSize() {
        return stateHolder.getPersistentStateSize();
    }

    public String getTableName(int index) {
        return tableNames.get(index);
    }

    public int getTableSize() {
        return tableNames.size();
    }

    public boolean isPropertyAccessed() {
        return propertyAccessed;
    }
}
