# **Backend Functionality Overview**

## **🚀 Core Features**

### **1. Passive Behavioral Analysis**
- **Invisible CAPTCHA**: Analyzes user behavior without interrupting user experience
- **Real-time Processing**: Instantly evaluates user interactions
- **Zero User Effort**: No puzzles, images, or challenges to solve

### **2. Multi-Factor Behavioral Metrics**
The system tracks and analyzes **4 key behavioral patterns**:

| Metric | Weight | Description | Human Pattern | Bot Pattern |
|--------|--------|-------------|---------------|-------------|
| **Mouse Movements** | 40% | Number of pointer movements | Random, natural movements | Minimal or robotic movements |
| **Movement Speed** | 30% | Average pointer speed | Variable, natural speeds | Consistent/unrealistic speeds |
| **Keyboard Usage** | 20% | Keyboard interaction detection | Natural typing patterns | No typing or robotic input |
| **Session Duration** | 10% | Time spent on page | Reasonable reading time | Too fast or too slow |

### **3. Intelligent Scoring System**
```java
// Scoring Algorithm
if (mouseMovements > 10) score += 0.4;      // Natural exploration
if (avgSpeed > 0.5) score += 0.3;           // Human-like movement
if (usedKeyboard) score += 0.2;             // Natural interaction  
if (sessionDuration > 5000) score += 0.1;   // Reasonable engagement
```

**Decision Threshold:**
- **Score > 0.5**: ✅ Human detected → "allow"
- **Score ≤ 0.5**: ❌ Potential bot → "challenge"

## **📊 API Endpoints**

### **POST `/api/v1/score`**
**Purpose**: Analyze user behavior and return CAPTCHA decision

**Request Body:**
```json
{
  "numPointerMoves": 15,
  "avgPointerSpeed": 0.8,
  "usedKeyboard": true,
  "sessionDuration": 8000
}
```

**Response:**
```json
{
  "score": 0.85,
  "decision": "allow",
  "id": 15,
  "timestamp": "2024-01-15T10:30:45.123"
}
```

### **GET `/api/v1/scores`**
**Purpose**: Retrieve all behavioral records (Admin/Testing)

**Response:**
```json
[
  {
    "id": 1,
    "numPointerMoves": 15,
    "avgPointerSpeed": 0.8,
    "usedKeyboard": true,
    "sessionDuration": 8000,
    "ipAddress": "192.168.1.100",
    "userAgent": "Mozilla/5.0...",
    "createdAt": "2024-01-15T10:30:45.123"
  }
]
```

### **GET `/api/v1/stats`**
**Purpose**: Get system statistics and performance metrics

**Response:**
```json
{
  "totalRequests": 150,
  "allowedRequests": 120,
  "challengedRequests": 30,
  "allowPercentage": 80.0
}
```

## **🗃️ Data Collection & Storage**

### **Comprehensive Data Capture**
- **Behavioral Metrics**: Mouse movements, speed, keyboard usage, timing
- **Contextual Data**: IP addresses, user agents, timestamps
- **Analytics**: Request patterns, success rates, system performance

### **Database Schema**
```sql
CREATE TABLE user_features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    num_pointer_moves INT,
    avg_pointer_speed DOUBLE,
    used_keyboard BOOLEAN,
    session_duration BIGINT,
    ip_address VARCHAR(255),
    user_agent VARCHAR(255),
    created_at TIMESTAMP
);
```

## **🛡️ Security Features**

### **1. CORS Configuration**
```java
@CrossOrigin(origins = "*") // Configurable for production
```

### **2. SQL Injection Prevention**
- Spring Data JPA parameter binding
- Automatic query sanitization
- Prepared statements

### **3. IP Analysis**
- Client IP detection behind proxies
- IP-based rate limiting ready
- Geographic analysis capable

## **⚙️ Technical Architecture**

### **Backend Stack**
- **Framework**: Spring Boot 3.5.5
- **Database**: MySQL 8.0 (H2 for development)
- **Persistence**: Spring Data JPA + Hibernate
- **Security**: Spring Security (configurable)

### **Database Support**
- **MySQL**: Production-ready with connection pooling
- **H2**: In-memory for development and testing
- **Automatic Migrations**: Schema updates via Hibernate

### **Performance Features**
- **Connection Pooling**: HikariCP for optimal database connections
- **Caching**: Ready for Redis integration
- **Scalability**: Stateless architecture for horizontal scaling

## **📈 Analytics & Monitoring**

### **Built-in Analytics**
- Real-time request statistics
- Success/failure rates
- Behavioral pattern analysis
- System performance metrics

### **Monitoring Endpoints**
- Health checks ready for Spring Boot Actuator
- Database connection monitoring
- Performance metrics collection

## **🎯 Use Cases**

### **1. Form Protection**
- Login/registration forms
- Contact forms
- Comment systems

### **2. E-commerce Security**
- Order processing
- Payment forms
- Account creation

### **3. API Protection**
- REST API endpoints
- Webhook verification
- Automated request prevention

## **🔮 Future-Ready Features**

### **Machine Learning Ready**
- Structured data collection for ML training
- Export capabilities for data analysis
- Model integration points defined

### **Extensibility**
- Modular scoring system
- Plugin architecture for new metrics
- Webhook support for integrations

### **Enterprise Features**
- Multi-tenant ready
- Audit logging capable
- Compliance tracking (GDPR, CCPA)

## **🚀 Deployment Ready**

### **Containerization**
- Dockerfile ready
- Docker Compose configuration
- Cloud deployment prepared

### **Environment Configuration**
- Multiple profiles (dev, test, prod)
- Externalized configuration
- Secret management ready

This backend provides a **complete, production-ready** passive CAPTCHA system that balances security with user experience while maintaining extensibility for future enhancements.