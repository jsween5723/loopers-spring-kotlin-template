CREATE INDEX idx_product_brand_state_disp_price
    ON product (brand_id, state, displayed_at DESC, price, id);
ALTER TABLE product_signal
    ADD UNIQUE uq_ps_product (product_id);
CREATE INDEX idx_product_signal_like_count
    ON product_signal (like_count);

ALTER TABLE product_like
    ADD UNIQUE KEY uq_product_like_product_user (product_id, user_id);

commit;