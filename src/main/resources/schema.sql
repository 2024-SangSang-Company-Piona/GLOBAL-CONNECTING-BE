-- 테이블 삭제
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS chatting;
DROP TABLE IF EXISTS member;

-- member 테이블 생성
CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255),
                        PRIMARY KEY (id)
) ENGINE=InnoDB;

-- chatting 테이블 생성
CREATE TABLE chatting (
                          chatting_id BIGINT NOT NULL AUTO_INCREMENT,
                          create_time DATETIME(6),
                          member_id BIGINT,
                          title VARCHAR(255),
                          PRIMARY KEY (chatting_id),
                          CONSTRAINT FK_chatting_member FOREIGN KEY (member_id) REFERENCES member (id)
) ENGINE=InnoDB;

-- message 테이블 생성
CREATE TABLE message (
                         chatting_chatting_id BIGINT,
                         message_id BIGINT NOT NULL AUTO_INCREMENT,
                         role VARCHAR(255),
                         content TEXT,
                         PRIMARY KEY (message_id),
                         CONSTRAINT FK_message_chatting FOREIGN KEY (chatting_chatting_id) REFERENCES chatting (chatting_id)
) ENGINE=InnoDB;
