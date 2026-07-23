# Passive CAPTCHA: A Behavioral Biometrics Approach to Bot Detection

This document serves as a comprehensive guide for writing a research paper based on the **Passive CAPTCHA** project. It outlines the structure of the paper, provides details on the system architecture, and gives suggestions on how to articulate the methodology, findings, and future scope.

---

## 1. Proposed Paper Title Ideas
- *Passive CAPTCHA: Enhancing User Experience through Behavioral Biometrics and Machine Learning*
- *Invisible Bot Detection: A Multi-Factor Behavioral Analysis approach using Spring Boot and FastAPI*
- *Beyond Visual Challenges: Implementing a Zero-Friction Passive CAPTCHA System*

---

## 2. Abstract Outline
- **Context:** Traditional CAPTCHAs (Completely Automated Public Turing test to tell Computers and Humans Apart) often degrade the user experience by introducing friction (e.g., selecting traffic lights or typing distorted characters).
- **Problem:** Balancing robust bot defense with seamless user experience is a significant challenge in modern web applications.
- **Solution:** Introduce the "Passive CAPTCHA" system, an invisible mechanism that evaluates user behavior in real-time without requiring puzzle-solving.
- **Methodology:** The system captures client-side behavioral metrics—mouse movements, movement speed, keyboard interactions, and session duration—and evaluates them using a hybrid approach of heuristic scoring and Machine Learning (Logistic Regression).
- **Results Expectation:** Mention that the system achieves high accuracy in distinguishing human patterns from automated scripts while maintaining zero user friction.

---

## 3. Introduction
- **The Evolution of CAPTCHA:** Begin with text-based CAPTCHAs, transition to image-based (reCAPTCHA v2), and finally invisible CAPTCHAs (reCAPTCHA v3/Enterprise).
- **The Problem with Active CAPTCHAs:** Discuss accessibility issues, cognitive load, and the arms race with sophisticated OCR and AI bots solving visual challenges.
- **Behavioral Biometrics:** Introduce the concept of identifying users based on *how* they interact with a device rather than *what* they input.
- **Project Contribution:** Formally introduce this project's architecture, emphasizing its lightweight nature, open-source tech stack (React, Spring Boot, FastAPI, Scikit-learn), and hybrid evaluation approach.

---

## 4. Background & Related Work
- Discuss existing literature on **Behavioral Biometrics** (keystroke dynamics, mouse dynamics).
- Mention differences between heuristic-driven approaches and machine-learning-based approaches for bot detection.
- Cite papers that discuss the UX impact of security measures.

---

## 5. System Architecture
Explain the three-tier architecture of the project:

### A. Frontend Data Collection (React + Vite)
- The client-side application acts as the sensory organ of the system.
- It silently monitors DOM events (e.g., `mousemove`, `keydown`).
- Data is aggregated over the session and transmitted securely to the backend upon a sensitive action (like form submission).

### B. Backend Processing & Storage (Spring Boot, Java, MySQL)
- Acts as the central orchestrator and data persistence layer.
- **Endpoints:** Exposes RESTful APIs (e.g., `POST /api/v1/score`).
- **Heuristic Fallback Engine:** Implements a rule-based scoring algorithm assigning weights to different behaviors to make immediate decisions.
- **Database:** Stores user features (IP, User-Agent, speed, movements) for auditing, analysis, and retrying ML models. (Uses Spring Data JPA with MySQL/H2).

### C. Machine Learning Microservice (Python, FastAPI, Scikit-learn)
- A decoupled service responsible for deep behavioral analysis.
- The backend sends the extracted features to this service (`POST /predict`).
- **Model Used:** It utilizes a scaled **Logistic Regression** model capable of identifying non-linear patterns between human characteristics that simple heuristics might miss.

---

## 6. Methodology & Feature Engineering
Detail the core metrics (features) collected and their significance. This is the most crucial part of the paper.

| Feature (Metric) | Description | Human Pattern | Bot / Script Pattern |
| :--- | :--- | :--- | :--- |
| **Mouse Movements (`numPointerMoves`)** | The organic trajectory of the mouse cursor. | Natural exploration, curves, hesitations. | often 0 (headless browsers) |
| **Movement Speed (`avgPointerSpeed`)** | Velocity of the cursor across the screen. | Variable speeds, naturally slowing down near targets. | Unrealistic speed (teleportation) or perfectly constant speed. |
| **Keyboard Usage (`usedKeyboard`)** | Keystroke detection during the session. | Varied cadence, natural pauses. | Often bypassed or injected directly into the DOM lacking native event triggers. |
| **Session Duration (`sessionDuration`)** | Total time taken before form submission. | Reasonable reading and interaction time (e.g., >3 seconds). | Near-instantaneous execution (e.g., <100ms). |

### The Hybrid Decision Engine
1. **Heuristic Engine Baseline:** 
   - Uses weighted scoring. E.g., natural mouse moves (+0.4), human-like speed (+0.3), keyboard use (+0.2), reasonable duration (+0.1).
   - *Threshold:* A score > 0.5 classifies the user as "Human".
2. **Machine Learning Classifier:**
   - Features undergo Standardization (`StandardScaler`).
   - A `LogisticRegression` model evaluates the feature vector and outputs a binary classification.
   - **Advantage:** The ML model can learn from historical data (via `train_model.py`) to adapt to new bot patterns.

---

## 7. Evaluation & Results (How to write this section)
*(Since you need to populate this based on your testing, use the following framework)*
- **Data Collection Scenario:** Describe how you gathered normal human data (e.g., asking 10 friends to use the app) and simulated bot data (e.g., using Selenium or Puppeteer to fill the form without natural mouse movements).
- **Performance Metrics:** 
   - **True Positive Rate (Sensitivity):** Percentage of humans accurately passed.
   - **True Negative Rate (Specificity):** Percentage of bots successfully blocked.
- **Comparison:** Compare the accuracy of the Heuristic Rule-based Engine vs. the Logistic Regression ML model.
- **Latency:** Mention the low latency of the system. E.g., the ML service adds only ~20-50ms of overhead, making the CAPTCHA truly "invisible" to the user.

---

## 8. Discussion & Future Work
- **Limitations:** Acknowledge that advanced bots (like Puppeteer with `ghost-cursor` libraries) might spoof some of these metrics.
- **Future Enhancements:**
  - Introduce **Touch Event Analysis** (swipes, taps) for mobile users.
  - Implement Deep Learning architectures (e.g., RNNs / LSTMs) to analyze sequential time-series data of mouse coordinates rather than just aggregated averages.
  - Add **Browser Fingerprinting** (Canvas hashing, WebGL data) as an additional security layer.

---

## 9. Conclusion
Summarize that the project proves it is entirely feasible to decouple security from user friction. The integration of a Spring Boot backend with a Python Machine Learning service provides a scalable, highly available architecture that protects web endpoints effectively using behavioral biometrics.

---

## Tips for Publishing / Presenting
1. **Include Architecture Diagrams:** Draw a simple block diagram showing the flow (React -> Spring Boot -> MySQL & FastAPI).
2. **Include Code Snippets:** Show the heuristic scoring logic in Java or the ML model training snippet in Python to demonstrate technical depth.
3. **Data Privacy Note:** Emphasize that the system *does not* collect Personally Identifiable Information (PII) for CAPTCHA purposes; it only collects metadata (speeds, counts, durations), ensuring GDPR/CCPA compliance.
