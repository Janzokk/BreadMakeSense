--
-- Base de dades: `BMS`
--

-- --------------------------------------------------------

--
-- Estructura de la taula `User`
--

create table users (
    id int(4) auto_increment,
    username varchar(35),
    passwd varchar(16) not null,
    bread double default 0,
    legacy_bread double default 0,
    ascend float default 1,
    constraint pk_user primary key(id, username)
)
--
-- Estructura de la taula `Items`
--

create table items (
    user_id int primary key,
    i1 int(3) default 0,
    i2 int(3) default 0,
    i3 int(3) default 0,
    i4 int(3) default 0
)

--
-- Estructura de la taula `Transactions`
--

create table transactions (
    id int(4) auto_increment primary key,
    trans_time timestamp,
    donator int(4) not null,
    recipient int(4) not null,
    bread bigint(10) not null
)

--
-- Estructura de la taula `Server`
--

create table server (
    vers varchar(3) primary key,
    breads bigint default 0
)

commit;
