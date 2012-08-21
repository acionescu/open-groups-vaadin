select * from access_permissions  

select * from action_types_permissions  

select * from action_types  

insert into access_permissions values(2,'ALL');

insert into access_rules_strategies(access_rule_id,user_type_id,allowed_permissions_id)  values(
(select id from access_rules where name='HIDDEN')
,(select id from user_types  where type='CREATOR')
,(select id from access_permissions  where name='ALL')
);

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

select * from access_rules


insert into user_types(type,description) values('OTHER','A user than is not the creator nor belongs to the group of an entity');


select * from access_rules_strategies  

select * from user_types

select ut.type from get_user_type_ids(2,23) cut, user_types ut
where ut.id=cut

select * from actions

 select * from is_action_type_allowed((select action_type_id from actions where action='ro.problems.flows.save-entity-link-user-data'),2,24);

 select actLog.* from activity_log actLog
 where actLog.entity_id=410
and actLog.action_timestamp = (select max(al2.action_timestamp) from activity_log al2 where al2.entity_id=actLog.entity_id )
 
(select distinct on (entity_id) * from activity_log) actLog on (
actLog.entity_id =410
and actLog.action_type_id in (select id from action_types where type in ('CREATE','UPDATE'))
and actLog.action_timestamp = (select max(al2.action_timestamp) from activity_log al2 where al2.entity_id=actLog.entity_id)
)

