create table ACT_ID_TENANT (
    ID_ varchar(255),
    REV_ integer,
    NAME_ varchar(255),
    primary key (ID_)
);

create table ACT_ID_TENANT_MEMBERSHIP (
    USER_ID_ varchar(255),
    TENANT_ID_ varchar(255),
    primary key (USER_ID_, TENANT_ID_)
);

create index ACT_IDX_TENANT_MEMBERSHIP on ACT_ID_TENANT_MEMBERSHIP(TENANT_ID_);
alter table ACT_ID_TENANT_MEMBERSHIP
    add constraint ACT_FK_MEMB_TENANT
    foreign key (TENANT_ID_)
    references ACT_ID_TENANT (ID_)
    ON DELETE CASCADE;

create index ACT_IDX_TENANT_USER on ACT_ID_TENANT_MEMBERSHIP(USER_ID_);
alter table ACT_ID_TENANT_MEMBERSHIP
    add constraint ACT_FK_MEMB_TEN_USER
    foreign key (USER_ID_)
    references ACT_ID_USER (ID_);