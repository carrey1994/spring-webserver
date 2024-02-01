WITH RECURSIVE RelationHierarchy AS (
  SELECT
    descendant_id,
    ancestor_id,
    1 AS LEVEL
  FROM
    relation
  WHERE
    relation.ancestor_id = UUID_TO_BIN(
      'C17881A141BC4B4B92745B3F30E96AEE'
    )
  UNION ALL
  SELECT
    r.descendant_id,
    r.ancestor_id,
    rh.level + 1
  FROM
    relation r
    INNER JOIN RelationHierarchy rh ON rh.descendant_id = r.ancestor_id
)
SELECT
  descendant_id,
  ancestor_id,
  user_profile.address,
  user_profile.user_id,
  user_profile.email,
  level
FROM
  RelationHierarchy
  INNER JOIN user_profile ON descendant_id = user_profile.user_id;
