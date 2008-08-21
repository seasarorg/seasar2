/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.jdbc.gen.internal.model;

import org.seasar.extension.jdbc.gen.desc.AssociationDesc;
import org.seasar.extension.jdbc.gen.desc.AssociationType;
import org.seasar.extension.jdbc.gen.model.AssociationModel;
import org.seasar.extension.jdbc.gen.model.AssociationModelFactory;
import org.seasar.extension.jdbc.gen.model.JoinColumnModel;

/**
 * @author taedium
 * 
 */
public class AssociationModelFactoryImpl implements AssociationModelFactory {

    public AssociationModel getAssociationModel(AssociationDesc associationDesc) {
        AssociationModel model = new AssociationModel();
        model.setName(associationDesc.getName());
        if (associationDesc.getAssociationType() == AssociationType.ONE_TO_MANY) {
            String entityName = associationDesc.getReferencedEntityDesc()
                    .getName();
            model.setShortClassName("List<" + entityName + ">");
        } else {
            model.setShortClassName(associationDesc.getReferencedEntityDesc()
                    .getName());
        }
        model.setAssociationType(associationDesc.getAssociationType());
        model.setMappedBy(associationDesc.getMappedBy());
        if (associationDesc.getColumnNameList().size() == 1) {
            String name = associationDesc.getColumnNameList().get(0);
            String referencedColumnName = associationDesc
                    .getReferencedColumnNameList().get(0);
            JoinColumnModel joinColumnModel = new JoinColumnModel();
            joinColumnModel.setName(name);
            joinColumnModel.setReferencedColumnName(referencedColumnName);
            model.setJoinColumnModel(joinColumnModel);
        } else if (associationDesc.getColumnNameList().size() > 1) {
            int i = 0;
            for (String name : associationDesc.getColumnNameList()) {
                String referencedColumnName = associationDesc
                        .getReferencedColumnNameList().get(i);
                JoinColumnModel joinColumnModel = new JoinColumnModel();
                joinColumnModel.setName(name);
                joinColumnModel.setReferencedColumnName(referencedColumnName);
                model.addJoinColumnModel(joinColumnModel);
            }
        }
        return model;
    }

}
