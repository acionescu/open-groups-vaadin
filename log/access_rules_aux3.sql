select * from actions

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

select * from access_rules  

select * from activity_log order by action_timestamp desc

select * from users

select get_max_allowed_access_level(661);


update entities 
set access_rule_id = get_max_allowed_access_level(id)
where id in
(
select c.id from children_of(641) c
, entities ce, access_rules car
where 
ce.id=c.id
and car.id= coalesce(ce.access_rule_id, (select id from access_rules where name='DEFAULT'))
and car.access_level > get_max_allowed_access_level(c.id)
)


alter table entities disable trigger entities_last_update_trigger;
alter table entities disable trigger log_activity_trigger;


alter table entities enable trigger entities_last_update_trigger;
alter table entities enable trigger log_activity_trigger;




select * from activity_log  where entity_id=661

select * from entities where id=661







 begin;

--update entity

update entities
set title='o postare personal? din interfa??'
,content='hmmmm<br><br>edit1 - fac postarea asta public?<br>edit2 - fac postarea asta din nou personal? <br><br>'
,access_rule_id=2
,group_id=null
where id = 641;

COMMIT;  
--update children in order not to violete max access level

update entities e
set access_rule_id = (select id from access_rules where access_level=get_max_allowed_access_level(e.id))
where id in
(
select c.id from children_of(641) c
, entities ce, access_rules car
where 
ce.id=c.id
and car.id= coalesce(ce.access_rule_id, (select id from access_rules where name='DEFAULT'))
and car.access_level > get_max_allowed_access_level(c.id)
);



select e.*,(select id from access_rules where access_level=get_max_allowed_access_level(e.id)) from entities e
where id in
(
select c.id from children_of(641) c
, entities ce, access_rules car
where 
ce.id=c.id
and car.id= coalesce(ce.access_rule_id, (select id from access_rules where name='DEFAULT'))
and car.access_level > get_max_allowed_access_level(c.id)
);





