# -- 파라미터
# SET @BRANDS := 200;                -- 생성할 브랜드 수
# SET @PRODUCTS_PER_BRAND := 5000;    -- 브랜드당 상품 수
# SET @LIKES_PER_PRODUCT := 5;       -- 상품당 좋아요 수(균등)
# SET @USERS := 50000;               -- user_id 범위
#
# SET SESSION cte_max_recursion_depth = 1000000;
#
# START TRANSACTION;
#
# -- 1) BRAND 생성 (CTE를 서브쿼리로 감싸서 INSERT)
#
# INSERT INTO brand (name, state, created_at, updated_at, deleted_at)
# WITH RECURSIVE seq(n) AS (
#     SELECT 1
#     UNION ALL
#     SELECT n + 1 FROM seq WHERE n < @BRANDS
# )
# SELECT
#     CONCAT('Brand_', n) AS name,
#     CASE WHEN RAND() < 0.7 THEN 'CLOSED' ELSE 'OPENED' END AS state,  -- CLOSED 약 70%
#     NOW(6) AS created_at,
#     NOW(6) AS updated_at,
#     NULL   AS deleted_at
# FROM seq;
#
# -- 2) PRODUCT 생성 (각 브랜드당 균등 개수)
# INSERT INTO product (
#     brand_id, name, price, stock, max_quantity,
#     created_at, updated_at, deleted_at, displayed_at, state
# )
# SELECT
#     b.id AS brand_id,
#     CONCAT('Product_', b.id, '_', ps.n) AS name,
#     ROUND(5000 + RAND() * 495000, 2) AS price,        -- 5천 ~ 50만 사이, 소수 2자리
#     FLOOR(RAND() * 1000) AS stock,                    -- 0 ~ 999
#     GREATEST(1, FLOOR(RAND() * 500)) AS max_quantity, -- 1 ~ 500
#     NOW(6) AS created_at,
#     NOW(6) AS updated_at,
#     NULL AS deleted_at,
#     DATE_SUB(
#             NOW(6),
#             INTERVAL FLOOR(RAND() * 90 * 24 * 60 * 60) SECOND
#     ) AS displayed_at,                                -- 최근 90일 내 임의 시각
#     IF(RAND() < 0.8, 'AVAILABLE', 'UNAVAILABLE') AS state
# FROM brand b
#          CROSS JOIN (
#     WITH RECURSIVE seq(n) AS (
#         SELECT 1
#         UNION ALL
#         SELECT n + 1 FROM seq WHERE n < @PRODUCTS_PER_BRAND
#     )
#     SELECT n FROM seq
# ) ps;
#
#
# -- 1) 헬퍼 테이블 생성
#
#
# -- 3) PRODUCT_LIKE 생성 (상품당 @LIKES_PER_PRODUCT개 균등 분포)
# INSERT INTO product_like (product_id, user_id, created_at, updated_at, deleted_at)
# SELECT
#     p.id,
#     1 + MOD(p.id * 131071 + ls.n * 524287, (SELECT @USERS)) AS user_id,
#     NOW(6), NOW(6),
#     CASE WHEN RAND() < (SELECT @DELETED_RATIO) THEN NOW(6) END AS deleted_at
# FROM product p
#          CROSS JOIN (
#     SELECT n
#     FROM (
#              WITH RECURSIVE seq(n) AS (
#                  SELECT 1
#                  UNION ALL
#                  SELECT n+1 FROM seq WHERE n < (SELECT @LIKES_PER_PRODUCT)
#              )
#              SELECT n FROM seq
#          ) x
# ) ls;
#
#
# INSERT INTO product_signal (created_at, deleted_at, updated_at, product_id, like_count)
# SELECT
#     NOW(6)              AS created_at,
#     NULL                 AS deleted_at,
#     NOW(6)              AS updated_at,
#     p.id                 AS product_id,
#     COUNT(pl.id)         AS like_count
# FROM product p
#          LEFT JOIN product_like pl
#                    ON p.id = pl.product_id
# GROUP BY p.id;
#
# COMMIT;
#
# -- 확인
# SELECT COUNT(*) AS brands   FROM brand;
# SELECT COUNT(*) AS products FROM product;
# SELECT COUNT(*) AS likes    FROM product_like;