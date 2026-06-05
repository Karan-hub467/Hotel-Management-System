CREATE DATABASE IF NOT EXISTS hotel_management
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel_management;

CREATE TABLE IF NOT EXISTS rooms (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type   ENUM('single', 'double', 'suite', 'deluxe') NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    capacity    INT NOT NULL DEFAULT 1,
    status      ENUM('available', 'booked', 'occupied', 'maintenance') DEFAULT 'available',
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS customers (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) UNIQUE,
    phone       VARCHAR(20) NOT NULL,
    address     TEXT,
    id_proof    VARCHAR(100),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS bookings (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    customer_id     INT NOT NULL,
    room_id         INT NOT NULL,
    check_in        DATE NOT NULL,
    check_out       DATE NOT NULL,
    status          ENUM('confirmed', 'checked_in', 'checked_out', 'cancelled') DEFAULT 'confirmed',
    total_amount    DECIMAL(10,2),
    payment_status  ENUM('pending', 'paid', 'refunded') DEFAULT 'pending',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    INDEX idx_room_date (room_id, check_in, check_out)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS payments (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    booking_id  INT NOT NULL,
    amount      DECIMAL(10,2) NOT NULL,
    method      ENUM('cash', 'card', 'upi', 'online') DEFAULT 'cash',
    status      ENUM('pending', 'completed', 'failed') DEFAULT 'completed',
    paid_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Seed rooms
INSERT INTO rooms (room_number, room_type, price, capacity, description) VALUES
('101', 'single', 1200.00, 1, 'Cozy single room with city view'),
('102', 'single', 1200.00, 1, 'Single room with garden view'),
('103', 'single', 1500.00, 1, 'Premium single room with balcony'),
('201', 'double', 2000.00, 2, 'Double room with queen bed'),
('202', 'double', 2200.00, 2, 'Double room with twin beds'),
('203', 'double', 2500.00, 2, 'Deluxe double room with sea view'),
('301', 'suite', 3500.00, 3, 'Executive suite with living area'),
('302', 'suite', 4000.00, 4, 'Family suite with kitchenette'),
('401', 'deluxe', 5000.00, 2, 'Deluxe room with jacuzzi'),
('402', 'deluxe', 5500.00, 2, 'Penthouse deluxe with panoramic view');
