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
  access_level bigint NOT NULL,
  name character varying(50) NOT NULL,
  active character(1) NOT NULL DEFAULT 'y'::bpchar,
  CONSTRAINT access_rules_pk PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE access_rules
  OWNER TO metaguvernare;
  

  
insert into access_rules(access_level,name) values(0,'PRIVATE');
insert into access_rules(access_level,name,active) values(1,'PRIVATELY_SHARED','n');
insert into access_rules(access_level,name,active) values(2,'PUBLICLY_SHARED','n');  
  

alter table entities add column access_rule_id bigint null;
alter table entities add constraint entities_access_rules_fk foreign key (access_rule_id) references access_rules (id);

alter table entities add column group_id bigint null;
alter table entities add constraint entity_group_fk foreign key (group_id) references entities (id);
  
