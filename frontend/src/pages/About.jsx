import "./About.css";

export default function About() {
  return (
    <div className="about-container">
      <div className="about-content">
        <h1>About Passive Captcha</h1>
        <p>
          Passive Captcha is a next-generation security solution that verifies
          human users without interrupting their browsing experience.
        </p>

        <p>
          Instead of showing frustrating puzzles, it analyzes user behavior and
          interaction patterns in the background to identify bots.
        </p>

        <p>
          This project aims to create a safer and smoother web experience for
          everyone — users, developers, and businesses.
        </p>

        <div className="about-features">
          <h2>Why Choose Passive Captcha?</h2>
          <ul>
            <li>Seamless user experience</li>
            <li>Advanced bot detection</li>
            <li>Easy integration</li>
            <li>High performance</li>
          </ul>
        </div>
      </div>
    </div>
  );
}