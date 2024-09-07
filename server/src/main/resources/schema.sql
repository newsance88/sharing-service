CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    255
) NOT NULL UNIQUE,
    CONSTRAINT pk_user PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    description
    VARCHAR
(
    255
) NOT NULL,
    requestor_id BIGINT NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY
(
    id
),
    CONSTRAINT fk_requestor FOREIGN KEY
(
    requestor_id
) REFERENCES users
(
    id
)
    );


CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    description TEXT,
    available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    request_id BIGINT ,
    CONSTRAINT fk_items_owner FOREIGN KEY
(
    owner_id
) REFERENCES users
(
    id
) ON DELETE CASCADE,
    CONSTRAINT fk_request FOREIGN KEY
(
    request_id
) REFERENCES requests
(
    id
)
  ON DELETE CASCADE,
    CONSTRAINT pk_items PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS booking
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    start_date
    TIMESTAMP
    NOT
    NULL,
    end_date
    TIMESTAMP
    NOT
    NULL,
    item_id
    BIGINT
    NOT
    NULL,
    user_id
    BIGINT
    NOT
    NULL,
    status
    VARCHAR
(
    255
) NOT NULL,
    CONSTRAINT fk_booking_item FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
) ON DELETE CASCADE,
    CONSTRAINT fk_booking_user FOREIGN KEY
(
    user_id
) REFERENCES users
(
    id
)
  ON DELETE CASCADE,
    CONSTRAINT pk_bookings PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    text
    VARCHAR
(
    255
) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT fk_comments_item FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
),
    CONSTRAINT fk_comments_author FOREIGN KEY
(
    author_id
) REFERENCES users
(
    id
),
    CONSTRAINT pk_comments PRIMARY KEY
(
    id
)

    );