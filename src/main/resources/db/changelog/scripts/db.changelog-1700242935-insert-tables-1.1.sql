INSERT INTO GULFNET_TMT_USER(USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, PHONE, LANGUAGE_PREFERENCE, STATUS, PROFILE_PHOTO, DATE_CREATED,CREATED_BY)
 VALUES ('ADMIN', 'AAAAAAAAAAAAAAAAAAAAAAZDA+Grs6j5yv3HdXSnltg=', 'ADMIN', 'ADMIN', 'admin@gulfnet.com','+919999999999','1','1','NULL', CURRENT_TIMESTAMP,'59415f196de26625344d9dbd');

INSERT INTO USER_ROLE(USER_ID, ROLE_ID)
  VALUES ((select id from public.GULFNET_TMT_USER where USER_NAME = 'ADMIN'), (select id from public.APP_ROLE where code = 'ADMIN'));
