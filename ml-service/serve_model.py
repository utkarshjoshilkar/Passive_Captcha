from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import numpy as np
import os

class Features(BaseModel):
    numPointerMoves: int
    avgPointerSpeed: float
    usedKeyboard: bool
    sessionDuration: int

app = FastAPI(title="PassiveCaptcha ML Service")

MODEL_PATH = os.environ.get("MODEL_PATH", "model/model.pkl")
SCALER_PATH = os.environ.get("SCALER_PATH", "model/scaler.pkl")

model = joblib.load(MODEL_PATH)
scaler = None
if os.path.exists(SCALER_PATH):
    scaler = joblib.load(SCALER_PATH)

@app.post("/predict")
def predict(f: Features):
    X = np.array([[f.numPointerMoves, f.avgPointerSpeed, 1 if f.usedKeyboard else 0, f.sessionDuration]], dtype=float)
    if scaler is not None:
        X = scaler.transform(X)

    if hasattr(model, "predict_proba"):
        proba = model.predict_proba(X)[0][1]
        return {"score": float(proba), "model_version": "v1"}
    else:
        pred = model.predict(X)[0]
        return {"score": float(pred), "model_version": "v1"}
