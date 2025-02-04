UPDATE clicks
SET referrer = subquery.referrer
    FROM (
    SELECT id,
           (ARRAY[
               'https://www.google.com',
               'https://www.facebook.com',
               'https://www.amazon.com',
               'https://twitter.com',
               'https://www.instagram.com',
               'https://mail.google.com',
               'https://stackoverflow.com',
               'Direct Traffic'
           ])[FLOOR(1 + RANDOM() * 8)::int] AS referrer
    FROM clicks
    WHERE referrer IS NULL
) AS subquery
WHERE clicks.id = subquery.id;


INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 14, '2025-01-22', id, slug, short_code FROM short_links WHERE id = 1;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 8, '2025-01-23', id, slug, short_code FROM short_links WHERE id = 1;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 1, '2025-01-24', id, slug, short_code FROM short_links WHERE id = 1;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 4, '2025-01-25', id, slug, short_code FROM short_links WHERE id = 1;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 6, '2025-01-26', id, slug, short_code FROM short_links WHERE id = 1;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 19, '2025-01-27', id, slug, short_code FROM short_links WHERE id = 1;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 14, '2025-01-28', id, slug, short_code FROM short_links WHERE id = 1;

INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 16, '2025-01-22', id, slug, short_code FROM short_links WHERE id = 2;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 16, '2025-01-23', id, slug, short_code FROM short_links WHERE id = 2;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 16, '2025-01-24', id, slug, short_code FROM short_links WHERE id = 2;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 7, '2025-01-25', id, slug, short_code FROM short_links WHERE id = 2;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 9, '2025-01-26', id, slug, short_code FROM short_links WHERE id = 2;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 3, '2025-01-27', id, slug, short_code FROM short_links WHERE id = 2;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 17, '2025-01-28', id, slug, short_code FROM short_links WHERE id = 2;

INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 3, '2025-01-22', id, slug, short_code FROM short_links WHERE id = 3;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 17, '2025-01-23', id, slug, short_code FROM short_links WHERE id = 3;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 2, '2025-01-24', id, slug, short_code FROM short_links WHERE id = 3;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 11, '2025-01-25', id, slug, short_code FROM short_links WHERE id = 3;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 1, '2025-01-26', id, slug, short_code FROM short_links WHERE id = 3;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 16, '2025-01-27', id, slug, short_code FROM short_links WHERE id = 3;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 9, '2025-01-28', id, slug, short_code FROM short_links WHERE id = 3;

INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 7, '2025-01-22', id, slug, short_code FROM short_links WHERE id = 4;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 6, '2025-01-23', id, slug, short_code FROM short_links WHERE id = 4;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 3, '2025-01-24', id, slug, short_code FROM short_links WHERE id = 4;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 7, '2025-01-25', id, slug, short_code FROM short_links WHERE id = 4;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 12, '2025-01-26', id, slug, short_code FROM short_links WHERE id = 4;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 4, '2025-01-27', id, slug, short_code FROM short_links WHERE id = 4;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 7, '2025-01-28', id, slug, short_code FROM short_links WHERE id = 4;

INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 7, '2025-01-22', id, slug, short_code FROM short_links WHERE id = 5;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 15, '2025-01-23', id, slug, short_code FROM short_links WHERE id = 5;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 12, '2025-01-24', id, slug, short_code FROM short_links WHERE id = 5;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 6, '2025-01-25', id, slug, short_code FROM short_links WHERE id = 5;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 6, '2025-01-26', id, slug, short_code FROM short_links WHERE id = 5;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 5, '2025-01-27', id, slug, short_code FROM short_links WHERE id = 5;
INSERT INTO click_summary (id, click_count, "date", short_link_id, slug, shortCode)
SELECT NEXTVAL('CLICK_SUM_SEQUENCE'), 2, '2025-01-28', id, slug, short_code FROM short_links WHERE id = 5;
