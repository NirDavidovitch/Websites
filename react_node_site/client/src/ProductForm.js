import { useState } from "react";

export default function ProductForm() {
  const [ServerResponse, setResponse] = useState("");
  const [productFormData, setProductFormData] = useState({
    companyId: "",
    productName: "",
    model: "",
    pid: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setProductFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    fetch("/product-data", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(productFormData),
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
        <h2>Register Your Products:</h2>
        <label htmlFor="companyId" className="form-label">
          Company ID:
        </label>
        <input
          type="text"
          id="companyId"
          name="companyId"
          className="form-input"
          value={productFormData.companyId}
          onChange={handleChange}
        />

        <label htmlFor="productName" className="form-label">
          Product Name:
        </label>
        <input
          type="text"
          id="productName"
          name="productName"
          className="form-input"
          value={productFormData.productName}
          onChange={handleChange}
        />

        <label htmlFor="model" className="form-label">
          Model:
        </label>
        <input
          type="text"
          id="model"
          name="model"
          className="form-input"
          value={productFormData.model}
          onChange={handleChange}
        />

        <label htmlFor="pid" className="form-label">
          pid:
        </label>
        <input
          type="text"
          id="pid"
          name="pid"
          className="form-input"
          value={productFormData.pid}
          onChange={handleChange}
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
