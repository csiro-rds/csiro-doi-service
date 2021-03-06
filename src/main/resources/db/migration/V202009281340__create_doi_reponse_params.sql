drop sequence DOI_RES_SEQ;

ALTER TABLE DOI_RESPONSE_PARAMS
DROP CONSTRAINT FK_USER_ID_DOI_RESPONSE;

drop table DOI_RESPONSE_PARAMS;


CREATE SEQUENCE  "DOI_RES_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

CREATE TABLE DOI_RESPONSE_PARAMS (
	ID NUMBER(19,0), 
    USER_ID NUMBER(19,0) not null,
    REQUEST_TYPE varchar2(25) not null,     
    RESPONSE char(1) CHECK(Response in ('Y','N')), 
    DOI varchar(255),
    URL varchar(255) not null,
    RESPONSE_TIME timestamp(9) not null
);

ALTER TABLE DOI_RESPONSE_PARAMS
ADD CONSTRAINT FK_USER_ID_DOI_RESPONSE
FOREIGN KEY (USER_ID) REFERENCES Users(ID);