CREATE DATABASE homemenuplanner_test;

create user 'test_user'@'%' identified by 'test_password';

GRANT ALL PRIVILEGES ON homemenuplanner_test.* TO 'test_user'@'%';

GRANT ALL PRIVILEGES ON homemenuplanner_test.* TO 'test_user'@'%';
FLUSH PRIVILEGES;