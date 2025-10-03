// API service for backend communication
const API_BASE_URL = 'http://localhost:8080/api/v1';

// Generic API request function
async function apiRequest(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  
  const config = {
    headers: {
      'Content-Type': 'application/json',
    },
    ...options,
  };

  try {
    const response = await fetch(url, config);
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    return await response.json();
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
}

// Score API
export const scoreAPI = {
  submitBehavior: (behaviorData) => 
    apiRequest('/score', {
      method: 'POST',
      body: JSON.stringify(behaviorData)
    }),

  getAllScores: () => 
    apiRequest('/scores'),

  getStats: () => 
    apiRequest('/stats'),

  healthCheck: () => 
    apiRequest('/health')
};

// Demo-specific API calls
export const demoAPI = {
  analyzeBehavior: async (behaviorData) => {
    try {
      const result = await scoreAPI.submitBehavior(behaviorData);
      return {
        success: true,
        data: result
      };
    } catch (error) {
      return {
        success: false,
        error: error.message
      };
    }
  }
};

export default apiRequest;