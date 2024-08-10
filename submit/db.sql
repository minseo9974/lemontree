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
                           `product_id`	INT	NOT NULL,
                           `type`	TINYINT	NOT NULL,
                           `money`	BIGINT	NOT NULL,
                           `created_at`	DATETIME	NOT NULL,
                           `status`	TINYINT	NOT NULL,
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

