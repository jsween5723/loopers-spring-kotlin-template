explain analyze
select *
from product
         right join product_signal
                    on product.id = product_signal.product_id
where brand_id = 2001
  and state = 'AVAILABLE'
order by product.displayed_at desc, product.price, product_signal.like_count
limit 20 offset 40
;

explain analyze
select *
from brand
where id = 2001;


explain analyze
select *
from product_like
where user_id = 1
  and product_id in (10000103, 10000104, 10000105, 10000106, 10000107, 10000108, 10000109);