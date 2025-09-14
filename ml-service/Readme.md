# Small README / How to run

### Locally (dev)

1. Train model (optional, or copy `model/model.pkl`):

   ```bash
   cd ml-service
   python -m pip install -r requirements.txt
   python train_model.py
   ```

   This creates `ml-service/model/model.pkl` and `scaler.pkl`.

2. Run ML service:

   ```bash
   uvicorn serve_model:app --reload --port 8000
   ```

3. Run Spring Boot (from `backend-server/`):

   ```bash
   mvn spring-boot:run
   ```

   Or build and run jar.

4. Test:
   POST JSON to `http://localhost:8080/api/v1/score`:

   ```json
   {
     "numPointerMoves": 12,
     "avgPointerSpeed": 0.7,
     "usedKeyboard": true,
     "sessionDuration": 6000
   }
   ```

   Backend will save record, call ML at `http://localhost:8000/predict`, and return `score` + `decision`.

### With Docker Compose

From repo root:

```bash
docker-compose up --build
```

Then test the same POST to `http://localhost:8080/api/v1/score`.

---
<!-- 
# Notes & best-practice reminders

* Keep the heuristic fallback inside `ScoreService` in case the ML service is down.
* Save scaler used during training and load it in `serve_model.py` — consistent preprocessing matters.
* Add model version to `/predict` response for traceability.
* For production, secure ML endpoint (auth), add rate-limiting, and persistent DB (Postgres).
* Log predictions + inputs (no PII) for later evaluation and retraining.

---

If you want, I can now:

* generate the full file contents for every file in the tree so you can copy/paste, **or**
* prepare a ready-to-run `docker-compose` repo files bundle (I’ll paste complete file contents), or
* add model versioning + simple health endpoint (`/health`) in FastAPI and show how ScoreService can circuit-break with Resilience4j.

Which of the above would you like next? -->
