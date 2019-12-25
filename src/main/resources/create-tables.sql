
/**/
create table price_tracking
(
    tracking_id int auto_increment,
    price float null,
    created_at timestamp default CURRENT_TIME() null,
    constraint price_tracking_pk
        primary key (tracking_id)
);




create table price_tracking_raw_data
(
	tracking_id int not null,
	data blob not null,
	constraint price_tracking_raw_data_pk
		primary key (tracking_id)
);



create table price_tracking_tracked_product
(
	product_id int auto_increment,
	url varchar(1024) not null,
	constraint price_tracking_tracked_product_pk
		primary key (product_id)
);

create unique index price_tracking_tracked_product_url_uindex
	on price_tracking_tracked_product (url);



create table price_tracking_site
(
	site_id int auto_increment,
	site_name varchar(1024) not null,
	url_regex varchar(1024) not null,
	price_regex varchar(1024) not null,
	constraint price_tracking_site_pk
		primary key (site_id)
);

