(function(window) {
    class PassiveCaptcha {
        constructor() {
            this.apiKey = null;
            this.events = {
                moves: 0,
                clicks: 0,
                keys: 0
            };
            this.startTime = Date.now();
            this.buffer = [];
            this.isInitialized = false;
        }

        init(apiKey) {
            this.apiKey = apiKey;
            this.isInitialized = true;
            this.startTracking();
            console.log('PassiveCAPTCHA initialized');
        }

        startTracking() {
            document.addEventListener('mousemove', (e) => {
                this.events.moves++;
                // In a real implementation, we would sample path data
                // this.buffer.push({ x: e.clientX, y: e.clientY, t: Date.now() });
            });

            document.addEventListener('click', () => {
                this.events.clicks++;
            });

            document.addEventListener('keydown', () => {
                this.events.keys++;
            });
        }

        async verify() {
            if (!this.isInitialized) {
                console.error('PassiveCAPTCHA not initialized. Call init(apiKey) first.');
                return null;
            }

            const payload = {
                numPointerMoves: this.events.moves,
                avgPointerSpeed: 0.5, // Mock calculation
                usedKeyboard: this.events.keys > 0,
                sessionDuration: Date.now() - this.startTime,
                timestamp: Date.now()
            };

            try {
                // In production, this URL would be the actual SaaS API endpoint
                const response = await fetch('http://localhost:8080/api/public/verify', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-API-KEY': this.apiKey,
                        // 'Origin' is set automatically by the browser
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    throw new Error('Verification failed');
                }

                const data = await response.json();
                return data;
            } catch (error) {
                console.error('PassiveCAPTCHA Error:', error);
                throw error;
            }
        }
    }

    // Expose to window
    window.PassiveCaptcha = new PassiveCaptcha();

})(window);
