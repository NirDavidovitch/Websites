const path = require("path");
const bodyParser = require("body-parser");
const express = require("express");

const sequelize = require("./util/database");
const Company = require("./models/company");
const Product = require("./models/product");

const PORT = process.env.PORT || 3001;

const app = express();

app.use(express.static(path.resolve(__dirname, "../client/build"))); // Have Node serve the files for our built React app
app.use(bodyParser.json()); // Parse JSON bodies
app.use(bodyParser.urlencoded({ extended: true })); // Parse URL-encoded bodies

Company.hasMany(Product);

sequelize.sync({ force: true }).catch((err) => {
  console.log(err);
});

app.get("/api", (req, res) => {
  res.json({ message: "Hello from server!" });
});

app.get("*", (req, res) => {
  res.sendFile(path.resolve(__dirname, "../client/build", "index.html"));
});

app.post("/company-data", (req, res) => {
  const { name, city, email, contactPhone, creditCard, expiryDate, cvv } =
    req.body;

  console.log("name ", name, "city ", city);

  Company.create({
    name: name,
    city: city,
    email: email,
    contactPhone: contactPhone,
    creditCard: creditCard,
    expiryDate: expiryDate,
    cvv: cvv,
  })
    .then((company) => {
      console.log("Company Registeration: ", company);
      const reqBody = company.toJSON();

      reqBody.command = "RegCompany";
      reqBody.priority = "1";

      fetch(`http://localhost:8080/gate`, {
        method: "POST",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
        body: JSON.stringify(reqBody),
      })
        .then((response) => response.json())
        .then((json) => console.log(json))
        .catch((err) => console.log(err));

      res.status(202).json({ message: company.id });
    })
    .catch((err) => {
      res.status(500).json({ message: err });
    });
});

app.post("/product-data", async (req, res) => {
  const { companyId, productName, model, pid } = req.body;

  const company = await Company.findByPk(companyId);
  if (company === null) {
    res.status(500).json({ message: "Company Not found!" });
  } else {
    company
      .createProduct({
        id: pid,
        productName: productName,
        model: model,
      })
      .then((product) => {
        console.log("Product Registeration: ", product);

        res.status(202).json({ message: "Product registered successfully!" });
      })
      .catch((err) => {
        res.status(500).json({ message: err });
      });
  }
});

app.listen(PORT, () => {
  console.log(`Server listening on ${PORT}`);
});
