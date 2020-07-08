CREATE TABLE IF NOT EXISTS `order` (
    `id`                BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `start_latitude`    VARCHAR(255) NOT NULL,
    `start_longitude`   VARCHAR(255) NOT NULL,
    `end_latitude`      VARCHAR(255) NOT NULL,
    `end_longitude`     VARCHAR(255) NOT NULL,
    `distance`          INT NOT NULL,
    `status`            enum ('UNASSIGNED', 'TAKEN') NOT NULL DEFAULT 'UNASSIGNED',
    `created_at`        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_updated_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;
