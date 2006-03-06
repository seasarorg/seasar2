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

import org.seasar.framework.ejb.unit.PersistentClass;
import org.seasar.framework.ejb.unit.PersistentClassNotFoundRuntimeException;
import org.seasar.framework.ejb.unit.PersistentState;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 *
 */
public abstract class AbstractPersistentState implements PersistentState {

	protected final String primaryTableName;

	protected final String stateName;

	protected final Class<?> stateType;

	protected String tableName;

	protected String columnName;

	protected boolean persistent;

	protected boolean relationship;

	protected boolean embedded;

	protected PersistentClass persistentStateHolder;

	protected Class collectionType;

	protected Map<String, Column> columnsByName = new HashMap<String, Column>();

	protected AbstractPersistentState(String stateName, Class<?> stateType,
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
		this.stateType = stateType;
		this.primaryTableName = primaryTableName;
	}

	protected void introspection(AnnotatedElement target) {
		if (target.isAnnotationPresent(Transient.class)) {
			return;
		}
		persistent = true;

		if (isRelationshipAnnotationPresent(target)) {
			setupRelationship();
		} else if (target.isAnnotationPresent(Embedded.class)) {
			setupEmbedded(target);
		} else {
			setupBasic(target);
		}
	}

	protected boolean isRelationshipAnnotationPresent(AnnotatedElement target) {
		return target.isAnnotationPresent(OneToOne.class)
				|| target.isAnnotationPresent(OneToMany.class)
				|| target.isAnnotationPresent(ManyToOne.class)
				|| target.isAnnotationPresent(ManyToMany.class);
	}

	protected void setupRelationship() {
		relationship = true;
	}

	protected void setupEmbedded(AnnotatedElement target) {
		embedded = true;
		tableName = primaryTableName.toUpperCase();
		AttributeOverride ao = target.getAnnotation(AttributeOverride.class);
		if (ao != null) {
			setupOverridedColumns(ao);
		}
		AttributeOverrides aos = target.getAnnotation(AttributeOverrides.class);
		if (aos != null) {
			for (AttributeOverride each : aos.value()) {
				if (each != null) {
					setupOverridedColumns(each);
				}
			}
		}
	}

	protected void setupOverridedColumns(AttributeOverride ao) {
		String name = ao.name();
		Column column = ao.column();
		columnsByName.put(name, column);
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

	public boolean hasTableName() {
		return tableName != null;
	}

	public String getTableName() {
		return tableName;
	}

	public boolean hasColumnName() {
		return columnName != null;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getStateName() {
		return stateName;
	}

	public Class<?> getStateType() {
		return stateType;
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

	public PersistentClass createPersistentClass() {
		if (isRelationship()) {
			if (isCollection()) {
				return new EntityClass(getCollectionType());
			}
			return new EntityClass(getStateType());
		} else if (isEmbedded()) {
			return new EmbeddableClass(getStateType(), getTableName(), isProperty(),
					Collections.unmodifiableMap(columnsByName));
		}
		throw new PersistentClassNotFoundRuntimeException(getStateType());
	}

	public abstract Object getValue(Object target);

	public abstract void setValue(Object target, Object value);

	public abstract boolean isProperty();
}
