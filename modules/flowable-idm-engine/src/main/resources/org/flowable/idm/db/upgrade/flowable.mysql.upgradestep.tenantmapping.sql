create table ACT_ID_TENANT (
    ID_ varchar(255),
    REV_ integer,
    NAME_ varchar(255),
    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_ID_TENANT_MEMBERSHIP (
    USER_ID_ varchar(255),
    TENANT_ID_ varchar(255),
    primary key (USER_ID_, TENANT_ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

alter table ACT_ID_TENANT_MEMBERSHIP
    add constraint ACT_FK_MEMB_TENANT
    foreign key (TENANT_ID_)
    references ACT_ID_TENANT (ID_)
    ON DELETE CASCADE;

alter table ACT_ID_TENANT_MEMBERSHIP
    add constraint ACT_FK_MEMB_TEN_USER
    foreign key (USER_ID_)
    references ACT_ID_USER (ID_);