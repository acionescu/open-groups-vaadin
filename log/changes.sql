update actions 
set params='[{name=sortList,value=e.last_update desc}]'
where name='entity.list.recent.activity';

--

DROP TABLE entities_tree;

-- Sequence: entities_links_seq

-- DROP SEQUENCE entities_links_seq;

CREATE SEQUENCE entities_links_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE entities_links_seq
  OWNER TO metaguvernare;


----

update actions 
set action='ro.problems.flows.save-entity-link-user-data'
where name='entity.vote';

---

alter table entity_types_relations  
add column allow_recursive_list character(1),
add column list_with_content character(1);

-----


update entity_types_relations etr
set allow_recursive_list='n'
,list_with_content='y'
where 
etr.source_entity_type_id=(select id from complex_entity_type where complex_type='SOLUTION') 
and etr.target_entity_type_id=(select id from complex_entity_type where complex_type='ISSUE');

---------


-- Table: entities_links

-- DROP TABLE entities_links;

CREATE TABLE entities_links
(
  entity_link_id bigint NOT NULL DEFAULT nextval('entities_links_seq'::regclass),
  entity_id bigint NOT NULL,
  parent_id bigint NOT NULL,
  absolute_depth integer,
  CONSTRAINT entities_links_pk PRIMARY KEY (entity_link_id ),
  CONSTRAINT entities_links_entities_child_fk FOREIGN KEY (entity_id)
      REFERENCES entities (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT entities_links_entities_parent_fk FOREIGN KEY (parent_id)
      REFERENCES entities (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE entities_links
  OWNER TO metaguvernare;


---

insert into entities_links (select nextval('entities_links_seq'),id,parent_entity_id from entities );


---------

WITH RECURSIVE subentities_list(id, complex_type_id,complex_type, parent_id, depth, path) AS (
        SELECT e.id, cet.id complex_type_id,cet.complex_type, el.parent_id, 0, ''||el.parent_id
        FROM entities e, complex_entity_type cet, entities_links el
        where el.parent_id=(select min(e5.id) from entities e5 where e5.id != e5.parent_entity_id)
        and el.entity_id=e.id
        and e.complex_entity_type_id=cet.id
        
      UNION ALL 
        SELECT e.id, cet.id complex_type_id,cet.complex_type, el.parent_id, eg.depth + 1, path||','||el.parent_id
        FROM entities e, subentities_list eg, complex_entity_type cet, entities_links el
        WHERE eg.id = el.parent_id
        and e.id=el.entity_id
        and e.complex_entity_type_id=cet.id
        --keep out circular links
        and path not like '%'||e.id||'%'
)      

--select * from entities_links

update entities_links el
set absolute_depth=(select depth from subentities_list sl where sl.id=el.entity_id and sl.parent_id=el.parent_id)
;

-- Table: entities_links_users

-- DROP TABLE entities_links_users;

CREATE TABLE entities_links_users
(
  entity_link_id bigint NOT NULL,
  user_id bigint NOT NULL,
  vote character(1),
  last_vote_update timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  CONSTRAINT entities_links_users_pk PRIMARY KEY (entity_link_id , user_id ),
  CONSTRAINT entities_links_users_entities_links_pk FOREIGN KEY (entity_link_id)
      REFERENCES entities_links (entity_link_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT entities_links_users_users_fk FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE entities_links_users
  OWNER TO metaguvernare;


-------

insert into entities_links_users
select 
el.entity_link_id,eu.user_id,eu.vote,eu.last_vote_update
from
entities_links el
,entities e
, entities_users eu
where
eu.entity_id=e.id
and el.entity_id=e.id
and el.parent_id=e.parent_entity_id;

----

-- View: entities_priorities

 DROP VIEW entities_priorities;


-- Function: get_general_priority(bigint)

DROP FUNCTION get_general_priority(bigint);

CREATE OR REPLACE FUNCTION get_general_priority(entityId bigint)
  RETURNS bigint AS
$BODY$
DECLARE 
	pr RECORD;
	output bigint :=0;
	p_sum numeric :=0;
	p_avg numeric :=0;
	users_count numeric:=0;
	total_users numeric:=0;
	prioritiezed_entities_count numeric :=0;
BEGIN

FOR pr IN SELECT * FROM entities_priorities_count epc where epc.entity_id=entityId LOOP 
	p_sum:=(p_sum + pr.priority*pr.count);
	users_count:=(users_count+pr.count);
END LOOP;
if(users_count > 0) then
p_avg:=(p_sum/users_count);
select into total_users count(*) from users;
--get the number of entities that got a priority from at least one user
select into prioritiezed_entities_count count(distinct(epc.entity_id)) from entities_priorities_count epc;
--output:=floor(11 - (11 - p_avg)*users_count/(total_users-1));
output:=floor(prioritiezed_entities_count+1.0 - prioritiezed_entities_count*((11.0 - p_avg)/10.0*users_count/(total_users-1.0)));
end if;	
RETURN output;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_general_priority(bigint)
  OWNER TO metaguvernare;



 

CREATE OR REPLACE VIEW entities_priorities AS 
 SELECT DISTINCT entities_users.entity_id, get_general_priority(entities_users.entity_id) AS priority
   FROM entities_users
  WHERE entities_users.priority IS NOT NULL;

ALTER TABLE entities_priorities
  OWNER TO metaguvernare;


-- Function: make_searchable(character varying)

-- DROP FUNCTION make_searchable(character varying);

CREATE OR REPLACE FUNCTION make_searchable(input_text character varying)
  RETURNS text AS
$BODY$begin
return translate(lower(input_text),'şţățîșâ','statisa');
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION make_searchable(character varying)
  OWNER TO metaguvernare;

--- remove show hierarchy actions
delete from action_strategies ast
where ast.action_id=(
select a.id from actions a
where name='entity.upstream.hierarchy'
);

--- login types
insert into application_config(param_name,string_value) 
values('login.types','{local={},openid={google=https://www.google.com/accounts/o8/id,yahoo=https://me.yahoo.com}}');

-- change users

update users 
set email='metaguvernare@gmail.com'
where username='metauser';

alter table users 
alter column username drop not null
,alter column password drop not null
,alter column email set not null;

--checkpoint--

-- 21.06.2012

-- Sequence: notification_modes_seq

-- DROP SEQUENCE notification_modes_seq;

CREATE SEQUENCE notification_modes_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE notification_modes_seq
  OWNER TO metaguvernare;
  
  
-- Table: notification_modes

-- DROP TABLE notification_modes;

CREATE TABLE notification_modes
(
  id bigint NOT NULL DEFAULT nextval('notification_modes_seq'::regclass),
  mode character varying(50),
  CONSTRAINT notification_modes_pk PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE notification_modes
  OWNER TO metaguvernare;

  
-- Table: user_notification_rules

-- DROP TABLE user_notification_rules;

CREATE TABLE user_notification_rules
(
  user_id bigint NOT NULL,
  entity_id bigint NOT NULL,
  action_type_id bigint NOT NULL,
  depth integer NOT NULL,
  notification_mode_id bigint NOT NULL,
  enabled character(1) NOT NULL DEFAULT 'y'::bpchar,
  CONSTRAINT user_notification_rules_pk PRIMARY KEY (user_id , entity_id , action_type_id ),
  CONSTRAINT user_notification_rules_action_types_fk FOREIGN KEY (action_type_id)
      REFERENCES action_types (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_notification_rules_entities_fk FOREIGN KEY (entity_id)
      REFERENCES entities (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_notification_rules_notification_modes_fk FOREIGN KEY (notification_mode_id)
      REFERENCES notification_modes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_notification_rules_users_fk FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_notification_rules
  OWNER TO metaguvernare;
  
  
ALTER TABLE action_types ADD COLUMN allow_notification character(1) DEFAULT 'n';

update action_types set allow_notification ='n';
update action_types set allow_notification='y'
where type in ('CREATE', 'UPDATE', 'VOTE');

ALTER TABLE action_types
   ALTER COLUMN allow_notification SET NOT NULL;
   
   
insert into notification_modes(mode) values('IMMEDIATELY');
insert into notification_modes(mode) values('DAILY');
insert into notification_modes(mode) values('WEEKLY');
insert into notification_modes(mode) values('MONTHLY');   

-- 24.06.2012 home


insert into action_targets values((select max(id)+1 from action_targets),'FOOTER:LEFT');
insert into action_types(id,type,description)  values((select max(id)+1 from action_types),'CONFIG','Generic config action');

insert into actions values(
(select max(id)+1 from actions)
,'ro.problems.flows.get-user-notifications-data'
,(select id from action_types where type='READ')
,'Get user notification rules data'
,'user.notification.rules.read'
,null);

insert into action_strategies values(
(select id from complex_entity_type where complex_type='*')
, (select id from user_types where type ='MEMBER')
, (select id from actions where name ='user.notification.rules.read')
, (select id from entity_types where type='*')
, (select id from action_targets where target='TAB')
,50
,(select id from complex_entity_type where complex_type='#')
,'n');

-- 14.07.2012
insert into application_config(param_name,int_value) values('immediate.notifications.age',5);
alter table application_config ADD column timestamp_value timestamp; 
insert into application_config(param_name,timestamp_value) values('last.immediate.notification.timestamp',localtimestamp);


-- Function: vote_log()

-- DROP FUNCTION vote_log();

CREATE OR REPLACE FUNCTION vote_log()
  RETURNS trigger AS
$BODY$begin
if(new.vote is not null)then
insert into activity_log(entity_id,action_type_id)
values(
(select entity_id from entities_links where entity_link_id=new.entity_link_id)
,(select id from action_types where type='VOTE')
);
end if;
return new;
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION vote_log()
  OWNER TO metaguvernare;
  
  
-- Trigger: vote_log on entities_links_users

-- DROP TRIGGER vote_log ON entities_links_users;

CREATE TRIGGER vote_log
  BEFORE INSERT OR UPDATE
  ON entities_links_users
  FOR EACH ROW
  EXECUTE PROCEDURE vote_log();
  
  
-- 16.07.2012

  insert into action_strategies  values((select id from complex_entity_type  where complex_type='METAGROUP')
,(select id from user_types where type='CREATOR'),(select id from actions where name='entity.update'),(select id from entity_types where type='METAGROUP')
,(select id from action_targets where target='TAB'),50,(select id from complex_entity_type  where complex_type='METAGROUP'),'f');

-- Function: get_direct_distance(bigint, bigint)

-- DROP FUNCTION get_direct_distance(bigint, bigint);

CREATE OR REPLACE FUNCTION get_direct_distance(child_id bigint, parent_id bigint)
  RETURNS bigint AS
$BODY$DECLARE depth bigint := -1;

begin

if(child_id=parent_id) then
	return 0;
end if;

WITH RECURSIVE entity_upstream_hierarchy(id, parent_entity_id, depth) AS (
        SELECT e.id, el.parent_id, 0
        FROM entities e, entities_links el
        where e.id=69
        and el.entity_id=e.id
        
      UNION ALL
        SELECT e.id, el.parent_id, eg.depth + 1
        FROM entities e, entity_upstream_hierarchy eg, entities_links el
        WHERE e.id = eg.parent_entity_id
	and el.entity_id=e.id
)

select into depth eh.depth from entities e, entity_upstream_hierarchy eh
where eh.id= parent_id;

if(depth is null) then
return -1;
end if;
--raise info 'depth: %',depth;

return depth;
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION get_direct_distance(bigint, bigint)
  OWNER TO metaguvernare;

