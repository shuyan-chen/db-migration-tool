CREATE TABLE IF NOT EXISTS User (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
    );


CREATE TABLE IF NOT EXISTS Profile (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT NOT NULL,
                                       bio TEXT,
                                       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS Post (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id INT NOT NULL,
                                    title VARCHAR(255) NOT NULL,
    content TEXT,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS Tag (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL UNIQUE
    );


CREATE TABLE IF NOT EXISTS Post_Tag (
                                        post_id INT NOT NULL,
                                        tag_id INT NOT NULL,
                                        FOREIGN KEY (post_id) REFERENCES Post(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tag(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
    );