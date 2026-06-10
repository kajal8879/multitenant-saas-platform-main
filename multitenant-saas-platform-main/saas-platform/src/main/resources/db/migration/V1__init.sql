create table organizations (
    id bigserial primary key,
    name varchar(255) not null,
    stripe_customer_id varchar(255),
    created_at timestamp with time zone
);

create table users (
    id bigserial primary key,
    email varchar(255) not null unique,
    name varchar(255),
    password_hash varchar(255),
    provider varchar(30) not null,
    role varchar(30) not null,
    created_at timestamp with time zone,
    organization_id bigint not null references organizations(id)
);

create table subscriptions (
    id bigserial primary key,
    organization_id bigint not null unique references organizations(id),
    plan varchar(30) not null,
    status varchar(30) not null,
    stripe_subscription_id varchar(255),
    current_period_end timestamp with time zone
);

create table feature_usage (
    id bigserial primary key,
    organization_id bigint not null references organizations(id),
    feature_key varchar(100) not null,
    used_count integer not null,
    constraint uk_feature_usage_org_feature unique (organization_id, feature_key)
);
