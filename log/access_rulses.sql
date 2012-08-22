begin;

delete from actions a where
a.name = 'user.notification.rules.read'
and not exists ( select 1 from action_strategies where action_id=a.id);

-- Function: entities_last_update()

-- DROP FUNCTION entities_last_update();

CREATE OR REPLACE FUNCTION entities_last_update()
  RETURNS trigger AS
$BODY$BEGIN
if(new.title != old.title or new.content != old.content) then
NEW.last_update := localtimestamp;
end if;
RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION entities_last_update()
  OWNER TO metaguvernare;

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
  
-- Table: access_rules

-- DROP TABLE access_rules;

CREATE TABLE access_rules
(
  id bigint NOT NULL DEFAULT nextval('access_rules_seq'::regclass),
  name character varying(50) NOT NULL,
  active character(1) NOT NULL DEFAULT 'y'::bpchar,
  access_level integer NOT NULL,
  CONSTRAINT access_rules_pk PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE access_rules
  OWNER TO metaguvernare;
  

-- Table: access_rules_strategies

-- DROP TABLE access_rules_strategies;

CREATE TABLE access_rules_strategies
(
  access_rule_id bigint NOT NULL,
  user_type_id bigint NOT NULL,
  allowed_permissions_id bigint,
  denied_permissions_id bigint,
  CONSTRAINT access_rules_strategies_pk PRIMARY KEY (access_rule_id , user_type_id ),
  CONSTRAINT access_rules_strategies_access_rules_fk FOREIGN KEY (access_rule_id)
      REFERENCES access_rules (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT access_rules_strategies_allowed_permissions_fk FOREIGN KEY (allowed_permissions_id)
      REFERENCES access_permissions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT access_rules_strategies_denied_permissions_id FOREIGN KEY (denied_permissions_id)
      REFERENCES access_permissions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT access_rules_strategies_usert_types_fk FOREIGN KEY (user_type_id)
      REFERENCES user_types (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE access_rules_strategies
  OWNER TO metaguvernare;

 
  

alter table entities add column access_rule_id bigint null;
alter table entities add constraint entities_access_rules_fk foreign key (access_rule_id) references access_rules (id);

alter table entities add column group_id bigint null;
alter table entities add constraint entity_group_fk foreign key (group_id) references entities (id);


insert into access_permissions values(1,'READONLY');

insert into action_types_permissions values((select id from access_permissions where name='READONLY'),(select id from action_types where type='READ'));

insert into access_rules(name,access_level) values('HIDDEN',0);

insert into access_rules(name,access_level) values('READONLY',1);

insert into access_permissions values(2,'ALL');

insert into access_rules_strategies(access_rule_id,user_type_id,allowed_permissions_id)  values(
(select id from access_rules where name='HIDDEN')
,(select id from user_types  where type='CREATOR')
,(select id from access_permissions  where name='ALL')
);


select setval('user_types_seq', (select max(id) from user_types), true);

insert into user_types(type,description) values('OTHER','A user than is not the creator nor belongs to the group of an entity');


insert into access_permissions values(3,'UPDATEONLY');

insert into action_types_permissions  values ((select id from access_permissions where name='UPDATEONLY'),(select id from action_types where type='UPDATE'));

insert into access_rules(name,active,access_level) values('DEFAULT','y',100);

-- default rule, any user can only read
insert into access_rules_strategies(access_rule_id,user_type_id,allowed_permissions_id)  values(
(select id from access_rules where name='DEFAULT')
,(select id from user_types where type='*') 
, (select id from access_permissions where name='READONLY')
);

-- default rule, creator can do anything
insert into access_rules_strategies(access_rule_id,user_type_id,allowed_permissions_id)  values(
(select id from access_rules where name='DEFAULT')
,(select id from user_types  where type='CREATOR')
,(select id from access_permissions  where name='ALL')
);

-- default rule, site members can do anything except update an entity that they did not create
insert into access_rules_strategies(access_rule_id,user_type_id,denied_permissions_id)  values(
(select id from access_rules where name='DEFAULT')
,(select id from user_types  where type='MEMBER')
,(select id from access_permissions  where name='UPDATEONLY')
);



-- Function: log_activity()

-- DROP FUNCTION log_activity();

CREATE OR REPLACE FUNCTION log_activity()
  RETURNS trigger AS
$BODY$BEGIN
IF(TG_OP='INSERT') THEN
INSERT INTO ACTIVITY_LOG(ENTITY_ID,ACTION_TYPE_ID)
VALUES(NEW.ID,(SELECT ID FROM ACTION_TYPES WHERE TYPE='CREATE'));
ELSIF(TG_OP='UPDATE' and ( new.title != old.title or new.content != old.content) ) THEN
INSERT INTO ACTIVITY_LOG(ENTITY_ID,ACTION_TYPE_ID)
VALUES(NEW.ID,(SELECT ID FROM ACTION_TYPES WHERE TYPE='UPDATE'));
END IF;
RETURN NEW;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION log_activity()
  OWNER TO metaguvernare;



update entities
set access_rule_id=(select id from access_rules where name='DEFAULT')
where access_rule_id is null;




--drop function children_of(bigint);
CREATE OR REPLACE FUNCTION children_of(start_id bigint)
   RETURNS TABLE (id bigint, owner_id bigint, complex_type_id bigint, parent_id bigint, parent_link_id bigint, cdepth integer, cpath text)
        AS 
$$

WITH RECURSIVE subentities_list(id, owner_id, complex_type_id, parent_id, parent_link_id, depth, path) AS (
        SELECT e.id, e.creator_id owner_id, e.complex_entity_type_id complex_type_id, el.parent_id, el.entity_link_id, 0, text ''||el.parent_id||'' 
        FROM entities e, entities_links el
        where el.parent_id=$1
        and el.entity_id=e.id
               
      UNION ALL 
        SELECT e.id, e.creator_id owner_id, e.complex_entity_type_id complex_type_id, el.parent_id, el.entity_link_id, eg.depth + 1, text ''||path||','||el.parent_id
        FROM entities e, subentities_list eg, entities_links el
        WHERE eg.id = el.parent_id
        and e.id=el.entity_id
        --keep out circular links
        and path not like '%'||e.id||'%'
        
       
)

select * from subentities_list;
$$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION children_of(bigint)
  OWNER TO metaguvernare;
  
  
-- get user types
--drop function get_user_type_ids(bigint, bigint);
CREATE OR REPLACE FUNCTION get_user_type_ids(user_id bigint, entity_id bigint) RETURNS setof bigint AS
$BODY$

declare 

user_type_ids bigint[];

begin
select user_type_ids||id into user_type_ids from user_types where type='*';

if not exists (select 1 from users where id=user_id) then
select user_type_ids||id into user_type_ids from user_types where type='GUEST';
else
select user_type_ids||id into user_type_ids from user_types where type='MEMBER';

if(entity_id is not null) then

if(user_id = (select creator_id from entities where id=entity_id)) then
select user_type_ids||id into user_type_ids from user_types where type='CREATOR';
else
select user_type_ids||id into user_type_ids from user_types where type='OTHER';
end if;

end if;


end if;

return query (select (user_type_ids)[s] user_type_id from generate_series(1,array_upper(user_type_ids, 1)) as s);
end;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

-- check if user is allowed to execute action
-- Function: is_action_type_allowed(bigint, bigint, bigint)

-- DROP FUNCTION is_action_type_allowed(bigint, bigint, bigint);

CREATE OR REPLACE FUNCTION is_action_type_allowed(IN action_type_id bigint, IN user_id bigint, IN entity_id bigint)
  RETURNS TABLE(allowed integer, access_rule_id bigint, user_type_id bigint, allowed_permissions_id bigint, denied_permissions_id bigint) AS
$BODY$
select 1 allowed, ars.access_rule_id,ars.user_type_id, ars.allowed_permissions_id, ars.denied_permissions_id
from 
entities e
, access_rules_strategies ars
left join action_types_permissions atp on atp.action_type_id=$1

where e.id=$3
and ((e.access_rule_id is null and ars.access_rule_id=(select id from access_rules where name='DEFAULT')) or (e.access_rule_id=ars.access_rule_id))
and ars.user_type_id in (select * from get_user_type_ids($2,$3))

and ( 
(ars.allowed_permissions_id is not null and ( (ars.allowed_permissions_id = (select id from access_permissions where name='ALL') ) or ( ars.allowed_permissions_id=atp.permission_id ) ) ) 
--)
or 
(ars.denied_permissions_id is not null and (ars.denied_permissions_id != (select id from access_permissions where name='ALL'))
and not exists (select 1 from action_types_permissions atp2 where atp2.permission_id=atp.permission_id and ars.denied_permissions_id=atp2.permission_id))
)
limit 1;

$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION is_action_type_allowed(bigint, bigint, bigint)
  OWNER TO metaguvernare;



update access_rules set active='n' where name='READONLY';

update actions
set params='[{name=title,is-required=true,is-form-field=true,field-width=60%,field-input-regex="^[\w\W]{2,120}$"},{name=content,is-required=true,is-form-field=true,field-input-regex="^[\w\W]{0,30000}$",field-ui-type=richtextarea,field-width=100%,field-height=100%},{name=access_rule_id,is-required=false, is-form-field=true,
field-ui-type=combobox,field-width=30%}]'
where name='entity.create';


update actions
set params='[{name=title,is-required=true,is-form-field=true,field-width=60%,field-input-regex="^[\w\W]{2,120}$"},{name=content,is-required=true,is-form-field=true,field-input-regex="^[\w\W]{0,30000}$",field-ui-type=richtextarea,field-width=100%,field-height=100%},{name=tags,is-required=true,is-form-field=true,field-width=30%},{name=access_rule_id,is-required=false, is-form-field=true,
field-ui-type=combobox,field-width=30%}]'
where name='entity.create.with_tags';

update actions
set params='[{name=title,is-required=true,is-form-field=true,field-width=60%,field-input-regex="^[\w\W]{2,120}$"},{name=content,is-required=true,is-form-field=true,field-input-regex="^[\w\W]{0,30000}$",field-ui-type=richtextarea,field-width=100%,field-height=100%},{name=access_rule_id,is-required=false, is-form-field=true,
field-ui-type=combobox,field-width=30%}]'
where name='entity.update';


insert into action_strategies values(
(select id from complex_entity_type where complex_type='COMMENT')
,(select id from user_types where type='CREATOR')
,(select id from actions where name='entity.update')
,(select id from entity_types where type='COMMENT')
,(select id from action_targets where target='TAB')
,55
,(select id from complex_entity_type where complex_type='COMMENT')
,'f'
);

-- make only metagroups issues and solutions capable of supporting notification rules


insert into action_strategies values(
(select id from complex_entity_type where complex_type='METAGROUP')
,(select id from user_types where type='MEMBER')
,(select id from actions where name='user.notification.rules.read')
,(select id from entity_types where type='METAGROUP')
,(select id from action_targets where target='TAB')
,50
,(select id from complex_entity_type where complex_type='#')
,'f'
);

insert into action_strategies values(
(select id from complex_entity_type where complex_type='ISSUE')
,(select id from user_types where type='MEMBER')
,(select id from actions where name='user.notification.rules.read')
,(select id from entity_types where type='ISSUE')
,(select id from action_targets where target='TAB')
,50
,(select id from complex_entity_type where complex_type='#')
,'f'
);


insert into action_strategies values(
(select id from complex_entity_type where complex_type='SOLUTION')
,(select id from user_types where type='MEMBER')
,(select id from actions where name='user.notification.rules.read')
,(select id from entity_types where type='SOLUTION')
,(select id from action_targets where target='TAB')
,50
,(select id from complex_entity_type where complex_type='#')
,'f'
);

delete from action_strategies acs
using actions a
where a.name='user.notification.rules.read'
and acs.action_id=a.id
and acs.complex_entity_type_id=(select id from complex_entity_type where complex_type='*');

commit;