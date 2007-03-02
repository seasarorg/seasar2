/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package examples.dao.tiger;

import org.seasar.dao.annotation.tiger.Column;

public class EmployeeSearchCondition {

    private Department department;

    private String job;

    private String dname;

    private String orderByString;

    /**
     * @return Returns the department.
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * @param department
     *            The department to set.
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * @return Returns the dname.
     */
    @Column("dname_0")
    public String getDname() {
        return dname;
    }

    /**
     * @param dname
     *            The dname to set.
     */
    public void setDname(String dname) {
        this.dname = dname;
    }

    /**
     * @return Returns the job.
     */
    public String getJob() {
        return job;
    }

    /**
     * @param job
     *            The job to set.
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * @return Returns the orderByString.
     */
    public String getOrderByString() {
        return orderByString;
    }

    /**
     * @param orderByString
     *            The orderByString to set.
     */
    public void setOrderByString(String orderByString) {
        this.orderByString = orderByString;
    }
}
