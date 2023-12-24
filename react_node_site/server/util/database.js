const Sequelize = require("sequelize");

const sequelize = new Sequelize("admin_db", "Nird221", "1234", {
  dialect: "mysql",
  host: "localhost",
});

module.exports = sequelize;
