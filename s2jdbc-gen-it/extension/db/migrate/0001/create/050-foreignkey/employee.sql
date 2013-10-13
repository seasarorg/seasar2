alter table EMPLOYEE add constraint EMPLOYEE_FK1 foreign key (DEPARTMENT_ID) references DEPARTMENT (ID);
alter table EMPLOYEE add constraint EMPLOYEE_FK2 foreign key (ADDRESS_ID) references ADDRESS (ID);
