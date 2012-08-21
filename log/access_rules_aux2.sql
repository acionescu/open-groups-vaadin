

drop function children_of(bigint);
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

select * from entities

select 1 from children_of(24) where owner_id != 2 limit 1;

-- get available access rules for entity to update

with max_parent_access_level as (select max(access_level) val from entities e2, access_rules ar2 where e2.id in (select parent_id from entities_links where entity_id=562) and ar2.id=e2.access_rule_id )

select ar.id,ar.name,ar.access_level from access_rules ar
where active='y'
and not exists ( select 1 from children_of(562) where owner_id != (select creator_id from entities where id=562) )
and ( (select val from max_parent_access_level) is null
or ar.access_level <= (select val from max_parent_access_level) )
order by ar.access_level


-- get user types
--drop function get_user_type_ids(bigint, bigint);
CREATE OR REPLACE FUNCTION get_user_type_ids(user_id bigint, entity_id bigint) RETURNS setof bigint AS
$BODY$

declare 

user_type_ids bigint[];

begin
select user_type_ids||id into user_type_ids from user_types where type='*';

if(user_id = null) then
select user_type_ids||id into user_type_ids from user_types where type='GUEST';
else
select user_type_ids||id into user_type_ids from user_types where type='MEMBER';

if(entity_id != null) then

if(user_id = (select creator_id from entities where id=entity_id)) then
select user_type_ids||id into user_type_ids from user_types where type='CREATOR';
else
select user_type_ids||id into user_type_ids from user_types where type='OTHER';
end if;

end if;


end if;

raise info ''||user_type_ids;

return query (select (user_type_ids)[s] user_type_id from generate_series(1,array_upper(user_type_ids, 1)) as s);
end;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;



select * from actions
select * from action_types

-- check if an user is allowed to execute action based on access rules input: userId, flowId

select * from access_permissions  
select * from access_rules_strategies 
select * from user_types


select 1 allowed, ars.access_rule_id,ars.user_type_id, ars.allowed_permissions_id, ars.denied_permissions_id
from 
entities e
, access_rules_strategies ars
left join action_types_permissions atp on atp.action_type_id=5

where e.id=24
and ((e.access_rule_id is null and ars.access_rule_id=(select id from access_rules where name='DEFAULT')) or (e.access_rule_id=ars.access_rule_id))
and ars.user_type_id in (select * from get_user_type_ids(2,24))

and ( 
(ars.allowed_permissions_id is not null and ( (ars.allowed_permissions_id = (select id from access_permissions where name='ALL') ) or ( ars.allowed_permissions_id=atp.permission_id ) ) ) 
--)
or 
(ars.denied_permissions_id is not null and (ars.denied_permissions_id != (select id from access_permissions where name='ALL'))
and not exists (select 1 from action_types_permissions atp2 where atp2.permission_id=atp.permission_id and ars.denied_permissions_id=atp2.permission_id))
)
limit 1;

select * from is_action_allowed('ro.problems.flows.get-entity-info-by-id',3,562);

select * from access_rules  

select * from entities where title='test3'

update entities set access_rule_id=(select id from access_rules  where name='HIDDEN')
where title='test3'

select * from entities;