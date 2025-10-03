from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import time

def test_real_bot():
    print("🚀 Starting Real Bot Test - Simulating actual bot behavior")
    
    # Setup Chrome
    chrome_options = Options()
    # chrome_options.add_argument("--headless")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--window-size=1920,1080")
    
    # Initialize driver
    service = Service(ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service, options=chrome_options)
    
    try:
        # Go to demo page
        print("📍 Opening demo page...")
        driver.get("http://localhost:3000/demo")
        time.sleep(1)  # Very short wait - bot-like
        
        # Bot behavior: Click immediately without any interactions
        print("🤖 Clicking immediately (bot behavior)...")
        analyze_button = driver.find_element(By.CLASS_NAME, "analyze-btn")
        analyze_button.click()
        
        # Wait for results
        print("⏳ Waiting for analysis...")
        time.sleep(4)
        
        # Get results
        result_element = driver.find_element(By.CLASS_NAME, "score-result")
        decision_element = driver.find_element(By.CLASS_NAME, "decision-badge")
        score_element = driver.find_element(By.CLASS_NAME, "score-value")
        
        decision = decision_element.text
        score = score_element.text
        
        print("\n" + "="*50)
        print("🤖 BOT TEST RESULTS")
        print("="*50)
        print(f"Human Score: {score}")
        print(f"Decision: {decision}")
        
        # Detailed analysis
        if "challenge" in decision.lower():
            print("✅ SUCCESS: Bot correctly detected and challenged!")
            print("🎯 Passive Captcha is WORKING!")
        elif "review" in decision.lower():
            print("⚠️  PARTIAL: Bot flagged for review")
            print("📈 Passive Captcha needs tuning")
        else:
            print("❌ FAIL: Bot was ALLOWED (not detected)!")
            print("🔧 Passive Captcha needs significant improvement")
            
        # Show what behavior was detected
        try:
            stats = driver.find_elements(By.CLASS_NAME, "stat")
            print("\n📊 Detected Behavior:")
            for stat in stats[:4]:  # Show first 4 stats
                print("  " + stat.text.replace('\n', ': '))
        except:
            pass
            
    except Exception as e:
        print(f"❌ Error: {e}")
        
    finally:
        print("\n⏳ Closing browser in 5 seconds...")
        time.sleep(5)
        driver.quit()
        print("🎯 Test completed!")

def test_multiple_bot_scenarios():
    """Test different bot scenarios"""
    scenarios = [
        ("Immediate click", 0.5),
        ("Very short session", 1),
        ("No interactions", 0.1),
    ]
    
    for scenario_name, wait_time in scenarios:
        print(f"\n🧪 Testing: {scenario_name}")
        
        chrome_options = Options()
        chrome_options.add_argument("--headless")
        chrome_options.add_argument("--no-sandbox")
        
        service = Service(ChromeDriverManager().install())
        driver = webdriver.Chrome(service=service, options=chrome_options)
        
        try:
            driver.get("http://localhost:3000/demo")
            time.sleep(wait_time)
            
            analyze_button = driver.find_element(By.CLASS_NAME, "analyze-btn")
            analyze_button.click()
            time.sleep(4)
            
            decision_element = driver.find_element(By.CLASS_NAME, "decision-badge")
            score_element = driver.find_element(By.CLASS_NAME, "score-value")
            
            decision = decision_element.text
            score = score_element.text
            
            print(f"  Result: {decision} (Score: {score})")
            
        except Exception as e:
            print(f"  Error: {e}")
        finally:
            driver.quit()

if __name__ == "__main__":
    # Run the main bot test
    test_real_bot()
    
    # Uncomment to run multiple scenarios
    # print("\n" + "="*50)
    # print("🔬 MULTIPLE BOT SCENARIOS")
    # print("="*50)
    # test_multiple_bot_scenarios()