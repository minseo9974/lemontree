CREATE TABLE `member` (
                          `member_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                          `status`	TINYINT	NOT NULL,
                          `balance`	BIGINT	NOT NULL	DEFAULT 0,
                          `max_balance`	BIGINT	NOT NULL,
                          `once_limit`	BIGINT	NOT NULL,
                          `day_limit`	BIGINT	NOT NULL,
                          `month_limit`	BIGINT	NOT NULL,
                          PRIMARY KEY (`member_id`)
);

CREATE TABLE `history` (
                           `history_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                           `member_id`	BIGINT	NOT NULL,
                           `order_id`	INT	NOT NULL,
                           `type`	TINYINT	NOT NULL,
                           `money`	BIGINT	NOT NULL,
                           `created_at`	DATETIME	NOT NULL,
                            PRIMARY KEY (`history_id`)
);

CREATE TABLE `amount_used` (
                               `amount_used_id`	BIGINT	NOT NULL AUTO_INCREMENT,
                               `member_id`	BIGINT	NOT NULL,
                               `day_amount_used`	BIGINT	NOT NULL,
                               `month_amount_used`	BIGINT	NOT NULL,
                               `last_update`	DATETIME	NOT NULL,
                               PRIMARY KEY (`amount_used_id`)
);


ALTER TABLE `history` ADD CONSTRAINT `FK_member_TO_history_1` FOREIGN KEY (
                                                                           `member_id`
    )
    REFERENCES `member` (
                         `member_id`
        );

ALTER TABLE `amount_used` ADD CONSTRAINT `FK_member_TO_amount_used_1` FOREIGN KEY (
                                                                                   `member_id`
    )
    REFERENCES `member` (
                         `member_id`
        );

# dummy data
insert into member (member_id, status, balance, max_balance, once_limit, day_limit,
                    month_limit) value (1999, 0, 10000, 15000, 3000, 10000, 15000);

insert into amount_used (amount_used_id, member_id, day_amount_used, month_amount_used,
                         last_update) VALUE (0, 1999, 0, 0, '2024-08-10 17:46:10');

insert into member (member_id, status, balance, max_balance, once_limit, day_limit,
                    month_limit) value (2000, 0, 15000, 20000, 4000, 11000, 20000);

insert into amount_used (amount_used_id, member_id, day_amount_used, month_amount_used,
                         last_update) VALUE (0, 2000, 0, 0, '2024-08-11 17:46:10');

insert into member (member_id, status, balance, max_balance, once_limit, day_limit,
                    month_limit) value (2001, 0, 25000, 30000, 6000, 16000, 29000);

insert into amount_used (amount_used_id, member_id, day_amount_used, month_amount_used,
                         last_update) VALUE (0, 2001, 0, 0, '2024-08-12 17:46:10');


