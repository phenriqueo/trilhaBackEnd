create sequence dimension_id_seq start with 1000 increment by 1  no minvalue no maxvalue cache 1;

create table dimension (
    id bigint NOT NULL DEFAULT nextval('dimension_id_seq'),
    name varchar(128),
    --is_native boolean not null,
    --primary_dimension_attribute_id bigint,
    CONSTRAINT dimension_pkey PRIMARY KEY (id)
    --CONSTRAINT uk_dimension_name UNIQUE (name)
);