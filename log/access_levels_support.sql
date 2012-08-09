-- Table: access_permissions

-- DROP TABLE access_permissions;

CREATE TABLE access_permissions
(
  id bigint NOT NULL,
  name character varying(50) NOT NULL,
  CONSTRAINT access_permissions_pk PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE access_permissions
  OWNER TO metaguvernare;



-- Sequence: access_rules_seq

-- DROP SEQUENCE access_rules_seq;

CREATE SEQUENCE access_rules_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE access_rules_seq
  OWNER TO metaguvernare;
  
  
  
  
-- Table: access_rules

-- DROP TABLE access_rules;

CREATE TABLE access_rules
(
  id bigint NOT NULL DEFAULT nextval('access_rules_seq'::regclass),
  name character varying(50) NOT NULL,
  group_permissions_id bigint,
  others_permissions_id bigint,
  active character(1) NOT NULL DEFAULT 'y'::bpchar,
  CONSTRAINT access_rules_pk PRIMARY KEY (id ),
  CONSTRAINT access_rules_groups_permissions_fk FOREIGN KEY (group_permissions_id)
      REFERENCES access_permissions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT access_rules_others_permissions_fk FOREIGN KEY (others_permissions_id)
      REFERENCES access_permissions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE access_rules
  OWNER TO metaguvernare;
 
-- Table: action_types_permissions

-- DROP TABLE action_types_permissions;

CREATE TABLE action_types_permissions
(
  permission_id bigint NOT NULL,
  action_type_id bigint NOT NULL,
  CONSTRAINT action_types_permissions_pk PRIMARY KEY (permission_id , action_type_id ),
  CONSTRAINT action_types_permissions_action_types_fk FOREIGN KEY (action_type_id)
      REFERENCES action_types (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT action_types_permissions_permissions_fk FOREIGN KEY (permission_id)
      REFERENCES access_permissions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action_types_permissions
  OWNER TO metaguvernare;


alter table entities add column access_rule_id bigint null;
alter table entities add constraint entities_access_rules_fk foreign key (access_rule_id) references access_rules (id);

alter table entities add column group_id bigint null;
alter table entities add constraint entity_group_fk foreign key (group_id) references entities (id);


insert into access_permissions values(1,'READONLY');

insert into action_types_permissions values((select id from access_permissions where name='READONLY'),(select id from action_types where type='READ'));

insert into access_rules(name,group_permissions_id,others_permissions_id) values('PRIVATE',null,null);

insert into access_rules(name,group_permissions_id,others_permissions_id) values('READONLY',(select id from access_permissions where name='READONLY'),(select id from access_permissions where name='READONLY'));


  
