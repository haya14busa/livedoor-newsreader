# --- !Ups

CREATE TABLE RELATEDOCS (
  guid bigint NOT NULL,
  rank1 bigint,
  rank2 bigint,
  rank3 bigint,
  rank4 bigint,
  rank5 bigint,
  PRIMARY KEY (guid)
);

# --- !Downs

DROP TABLE RELATEDOCS;
