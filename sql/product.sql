drop table product;

--Table 생성
create table product(
    pid  number(10),
    pname       varchar(30),
    quantity    number(10),
    price       number(10)
);
--기본키 생성
alter table product add constraint product_product_id_pk primary key(pid);

--유니크 생성
alter table product add constraint product_pname_unique UNIQUE (pname);

--NOT NULL 생성 [ pname, quantity, price ]
alter table product modify pname constraint product_pname_nn not null;
alter table product modify quantity constraint product_quantity_nn not null;
alter table product modify price constraint product_price_nn not null;

--시퀀스 생성
drop sequence product_pid_seq;
create sequence product_pid_seq;


commit;