# --- !Ups

CREATE TABLE ARTICLES (
  guid bigint NOT NULL,
  title varchar(200) NOT NULL,
  description varchar(2000) NOT NULL,
  pubDate timestamp NOT NULL,
  link varchar(200) NOT NULL,
  content text NOT NULL,
  html text NOT NULL,
  image varchar(200),
  PRIMARY KEY (guid)
);

# --- !Downs

DROP TABLE ARTICLES;
