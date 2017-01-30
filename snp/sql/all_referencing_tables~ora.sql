SELECT * 
  FROM all_constraints 
 WHERE upper(r_owner) = '<owner-user>' 
   AND upper(constraint_type) = 'R' 
   AND r_constraint_name IN (SELECT constraint_name 
                               FROM all_constraints 
                              WHERE upper(table_name) = '<root-table>' 
                                AND upper(constraint_type) IN ( 'U', 'P' ));
