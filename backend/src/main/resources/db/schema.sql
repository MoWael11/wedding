CREATE TABLE app_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wedding_event (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(16) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    event_date DATE,
    total_bytes INT NOT NULL DEFAULT 0,
    owner_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE person (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    wedding_event_id INT NOT NULL,
    FOREIGN KEY (wedding_event_id) REFERENCES wedding_event(id) ON DELETE CASCADE
);

CREATE TABLE image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,

    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    relative_path VARCHAR(500) NOT NULL,

    mime_type VARCHAR(50) NOT NULL,
    size_bytes INT NOT NULL,

    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (event_id) REFERENCES wedding_event(id) ON DELETE CASCADE
);

CREATE TABLE token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(64) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);
