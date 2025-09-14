import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
import joblib
from sklearn.preprocessing import StandardScaler

# Synthetic example - replace with your labeled data
X = np.random.rand(1000, 4)
X[:,0] = (X[:,0] * 30).astype(int)            # pointer moves
X[:,1] = X[:,1] * 2.0                         # avg speed
X[:,2] = (X[:,2] > 0.5).astype(int)           # used keyboard
X[:,3] = (X[:,3] * 15000).astype(int)         # session duration (ms)

# Synthetic label: adapt to your real labeling logic
y = ((X[:,0] > 10).astype(int) + (X[:,1] > 0.5).astype(int) + (X[:,2] == 1).astype(int)) > 1
y = y.astype(int)

# scale numeric features (save scaler)
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2, random_state=42)

model = LogisticRegression(max_iter=2000)
model.fit(X_train, y_train)

print("Train score:", model.score(X_train, y_train))
print("Test score:", model.score(X_test, y_test))

joblib.dump(model, "model/model.pkl")
joblib.dump(scaler, "model/scaler.pkl")
print("Saved model and scaler in model/")
