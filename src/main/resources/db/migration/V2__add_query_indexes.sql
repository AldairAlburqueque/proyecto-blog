CREATE INDEX idx_blog_created_at
    ON blog (created_at DESC);

CREATE INDEX idx_blog_category_created_at
    ON blog (id_category, created_at DESC);

CREATE INDEX idx_blog_user_created_at
    ON blog (id_user, created_at DESC);

CREATE INDEX idx_comment_blog_created_at
    ON comment (blog_id, created_at DESC);

CREATE INDEX idx_comment_user_id
    ON comment (user_id);