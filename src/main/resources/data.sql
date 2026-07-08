-- sample users (for reference)

INSERT INTO users (id, name, email, phone_number)
VALUES
(1, 'Swapnil Thakur', 'swapnil@example.com', '9876543210');

INSERT INTO users (id, name, email, phone_number)
VALUES
(2, 'Rahul Sharma', 'rahul@example.com', '9123456789');


-- user Preferences

INSERT INTO user_preferences
(id, user_id, email_enabled, sms_enabled, push_enabled, in_app_enabled)
VALUES
(1, 1, TRUE, FALSE, TRUE, TRUE);

INSERT INTO user_preferences
(id, user_id, email_enabled, sms_enabled, push_enabled, in_app_enabled)
VALUES
(2, 2, TRUE, TRUE, FALSE, TRUE);