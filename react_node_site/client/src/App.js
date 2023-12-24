import logo from "./images/logo.png";
import arrow from "./images/arrow.png";

import CompanyForm from "./CompanyForm";
import ProductForm from "./ProductForm";
import React from "react";
import "./App.css";

function App() {
  const [appContent, setContent] = React.useState(MainContent);

  function MainContent() {
    return (
      <>
        <img src={logo} alt="MyLogo" className="logo" />
        <h1>Welcome to My Website</h1>
        <p>Explore the limitless infrastructure capabilities for IoTs</p>

        <div className="buttons">
          <button className="btn" onClick={() => setContent(<CompanyForm />)}>
            <div className="btn-content">
              <div className="btn-icon">Register Company</div>
              <div className="btn-desc">
                Register Your Company to our services
              </div>
            </div>
          </button>

          <button className="btn" onClick={() => setContent(<ProductForm />)}>
            <div className="btn-content">
              <div className="btn-icon">Register Product</div>
              <div className="btn-desc">
                Already registered? Add Your Products Here!
              </div>
            </div>
          </button>
        </div>
      </>
    );
  }

  return (
    <div className="background-container">
      <div className="left-background">
        <img
          alt=""
          src={arrow}
          className="btn-back"
          onClick={() => setContent(MainContent)}
        />
      </div>
      <div className="right-background">
        <div className="container">{appContent}</div>
      </div>
    </div>
  );
}

export default App;
