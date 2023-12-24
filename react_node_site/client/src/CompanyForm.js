import { useState } from "react";

export default function CompanyForm() {
  const [ServerResponse, setResponse] = useState("");
  const [companyFormData, setCompanyFormData] = useState({
    name: "",
    city: "",
    email: "",
    contactPhone: "",
    creditCard: "",
    expiryDate: "",
    cvv: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setCompanyFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    fetch("/company-data", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(companyFormData),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("Response from the server:", data);
        setResponse(data.message);
      })
      .catch((error) => console.error("Error:", error));
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <h2>Register Your Company:</h2>
        <label htmlFor="name" className="form-label">
          Name:
        </label>
        <input
          type="text"
          id="name"
          name="name"
          className="form-input"
          value={companyFormData.name}
          onChange={handleChange}
        />

        <label htmlFor="city" className="form-label">
          City:
        </label>
        <input
          type="text"
          id="city"
          name="city"
          className="form-input"
          value={companyFormData.city}
          onChange={handleChange}
        />

        <label htmlFor="email" className="form-label">
          Email:
        </label>
        <input
          type="email"
          id="email"
          name="email"
          className="form-input"
          value={companyFormData.email}
          onChange={handleChange}
        />

        <label htmlFor="contactPhone" className="form-label">
          Contact Phone:
        </label>
        <input
          type="text"
          id="contactPhone"
          name="contactPhone"
          className="form-input"
          value={companyFormData.contactPhone}
          onChange={handleChange}
          pattern="[0-9]{10}"
        />

        <label htmlFor="creditCard" className="form-label">
          Credit Card Number:
        </label>
        <input
          type="text"
          id="creditCard"
          name="creditCard"
          className="form-input"
          value={companyFormData.creditCard}
          onChange={handleChange}
          pattern="[0-9]{16}"
        />

        <label htmlFor="expiryDate" className="form-label">
          expiry Date:
        </label>
        <input
          type="text"
          id="expiryDate"
          name="expiryDate"
          className="form-input"
          value={companyFormData.expiryDate}
          onChange={handleChange}
          pattern="(0[1-9]|1[0-2])\/[0-9]{2}"
          placeholder="MM/YY"
        />

        <label htmlFor="cvv" className="form-label">
          cvv:
        </label>
        <input
          type="text"
          id="cvv"
          name="cvv"
          className="form-input"
          value={companyFormData.cvv}
          onChange={handleChange}
          pattern="[0-9]{3}"
        />

        <button type="submit" className="btn-submit">
          Submit
        </button>
      </form>
      <label name="server-response" className="server-response">
        {ServerResponse}
      </label>
    </>
  );
}
