package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.Method;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.ejb.unit.AnnotationNotFoundRuntimeException;
import org.seasar.framework.ejb.unit.PersistentClass;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 *
 */
public class EntityClass extends AbstractPersistentClass implements
		PersistentClass {

	public EntityClass(Class<?> entityClass) {
		super(entityClass);
		Entity entity = entityClass.getAnnotation(Entity.class);
		if (entity == null) {
			throw new AnnotationNotFoundRuntimeException("@Entity", entityClass
					.getName());
		}
		name = StringUtil.isEmpty(entity.name()) ? ClassUtil
				.getShortClassName(entityClass) : entity.name();
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(holderType);
		setupSuperClasses();
		setupTableNames();
		setupAccessType(beanDesc);
		setupPersistenceState(beanDesc);
	}

	private void setupSuperClasses() {
	}

	private void setupAccessType(BeanDesc beanDesc) {
		for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
			PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
			if (propertyDesc.hasReadMethod()) {
				Method m = propertyDesc.getReadMethod();
				if (m.isAnnotationPresent(Id.class)
						|| m.isAnnotationPresent(EmbeddedId.class)) {
					propertyAccessed = true;
					return;
				}
			}
		}
	}

	private void setupTableNames() {
		Table primary = holderType.getAnnotation(Table.class);
		if (primary == null || StringUtil.isEmpty(primary.name())) {
			tableNames.add(name);
		} else {
			tableNames.add(primary.name());
		}

		SecondaryTable secondary = holderType
				.getAnnotation(SecondaryTable.class);
		if (secondary != null) {
			tableNames.add(secondary.name());
		}

		SecondaryTables secondaries = holderType
				.getAnnotation(SecondaryTables.class);
		if (secondaries != null) {
			for (SecondaryTable each : secondaries.value()) {
				tableNames.add(each.name());
			}
		}
	}
}
