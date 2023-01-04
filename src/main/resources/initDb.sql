create sequence dimension_id_seq start with 1000 increment by 1  no minvalue no maxvalue cache 1;

create table dimension (
    id bigint NOT NULL DEFAULT nextval('dimension_id_seq'),
    name varchar(128),
    data_type varchar(8),
    sonid bigint,
    CONSTRAINT dimension_pkey PRIMARY KEY (id)
);