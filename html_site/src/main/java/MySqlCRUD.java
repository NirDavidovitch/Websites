import java.sql.*;
import java.time.LocalDate;

public class MySqlCRUD {
	private static MySqlCRUD mySqlCRUD = null; 
	private Connection connection = null;
	
	private PreparedStatement insertCompany = null;
	private PreparedStatement readCompany = null;
	private PreparedStatement updateCompany = null;
	private PreparedStatement deleteCompany = null;
	
	private PreparedStatement insertProduct = null;
	private PreparedStatement readProduct = null;
	private PreparedStatement updateProduct = null;
	private PreparedStatement deleteProduct = null;

	private MySqlCRUD() {}
	
	public static MySqlCRUD getInstance(String dbURL, String userName, String password) {
		if (mySqlCRUD == null) {
			synchronized (MySqlCRUD.class) {
				if (mySqlCRUD == null) {
					try {
						mySqlCRUD = new MySqlCRUD();
						
						mySqlCRUD.connection = DriverManager.getConnection(dbURL, userName, password);
						mySqlCRUD.connection.setAutoCommit(false);
						
						mySqlCRUD.insertCompany = mySqlCRUD.connection.prepareStatement("INSERT INTO Company (name, city, contact, creding_card, exp, cvv) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
						mySqlCRUD.readCompany = mySqlCRUD.connection.prepareStatement("SELECT * FROM Company WHERE cid = ?");
						mySqlCRUD.updateCompany = mySqlCRUD.connection.prepareStatement("UPDATE Company SET ? = ? WHERE cid = ?");
						mySqlCRUD.deleteCompany = mySqlCRUD.connection.prepareStatement("DELETE FROM Company WHERE cid = ?");
						
						mySqlCRUD.insertProduct = mySqlCRUD.connection.prepareStatement("INSERT INTO Product (name, model, date) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
						mySqlCRUD.readProduct = mySqlCRUD.connection.prepareStatement("SELECT * FROM Product WHERE pid = ?");
						mySqlCRUD.updateProduct = mySqlCRUD.connection.prepareStatement("UPDATE Product SET ? = ? WHERE pid = ?");
						mySqlCRUD.deleteProduct = mySqlCRUD.connection.prepareStatement("DELETE FROM Product WHERE pid = ?");
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
			
		}
		
		return mySqlCRUD;
	}
	
	public static MySqlCRUD getInstance() {
		return mySqlCRUD;
	}
	
	public void close() {
		try {
			insertCompany.close();
			readCompany.close();
			updateCompany.close();
			deleteCompany.close();
			
			insertProduct.close();
			readProduct.close();
			updateProduct.close();
			deleteProduct.close();
			
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createProductsTable(String company) {
		String tableName = getTableName(company);
		// Create a Statement
        Statement statement;
        
		try {
			statement = connection.createStatement();
			
			String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
					+ "ID INT AUTO_INCREMENT PRIMARY KEY, "
					+ "ProductID VARCHAR(50), "
					+ "ProductName VARCHAR(255), "
					+ "ModelYear INT)";
			
			// Execute the SQL statement
			statement.executeUpdate(createTableSQL);
			
			// Close the Statement and Connection
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getTableName(String company) {
		return company + "_Products";
	}
	
	/********************************* Company CRUD ******************************/

	public int createCompany(String name, String city, String contactNum, String creditCard, String exp, String cvv) {
		int cid = 0;
		try {
			insertCompany.setString(1, name);
			insertCompany.setString(2, city);
			insertCompany.setString(3, contactNum);
			insertCompany.setString(4, creditCard);
			insertCompany.setString(5, exp);
			insertCompany.setString(6, cvv);

			int affectedRows = insertCompany.executeUpdate();
			if (1 == affectedRows) {
				ResultSet generatedKeys = insertCompany.getGeneratedKeys();
				if (generatedKeys.next()) {
					cid = generatedKeys.getInt(1);
				}
			}
			
			createProductsTable(name);

			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			e.printStackTrace();
		}

		return cid;
	}

	public void readCompany(int cid) {
		try {
			readCompany.setInt(1, cid);

			ResultSet resultSet = readCompany.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); ++i) {
				System.out.format("\033[4m%30s|\033[0m", metaData.getColumnName(i));
			}
			System.out.println();

			while (resultSet.next()) {
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i) {
					System.out.format("%30s|", resultSet.getString(i));
				}
				System.out.println();
			}
			System.out.println();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int updateCompanyLocation(int cid, String city) {
		int affectedRows = 0;
		try {
			updateCompany.setString(1, city);
			updateCompany.setInt(2, cid);

			affectedRows = updateCompany.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return affectedRows;
	}

	public int updateCompanyContact(int cid, String contactNum) {
		int affectedRows = 0;
		try {
			updateCompany.setString(1, contactNum);
			updateCompany.setInt(2, cid);

			affectedRows = updateCompany.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return affectedRows;
	}

	public int deleteCompany(int cid) {
		int affectedRows = 0;
		
		try {
			deleteCompany.setInt(1, cid);

			affectedRows = deleteCompany.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return affectedRows;
	}
	
	/***************************** Product CRUD **************************/
	
	public int createProduct(String company, String productID, String productName, String modelYear) {
		int pid = 0;
		
		try (PreparedStatement insertProduct = connection.prepareStatement("INSERT INTO " + getTableName(company) + " (productID, productName, modelYear) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
			
			insertProduct.setString(1, productID);
			insertProduct.setString(2, productName);
			insertProduct.setString(3, modelYear);

			int affectedRows = insertProduct.executeUpdate();
			if (1 == affectedRows) {
				ResultSet generatedKeys = insertProduct.getGeneratedKeys();
				if (generatedKeys.next()) {
					pid = generatedKeys.getInt(1);
				}
			}

			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			e.printStackTrace();
		}

		return pid;
	}

	public void readProduct(int pid) {
		try {
			readProduct.setInt(1, pid);

			ResultSet resultSet = readProduct.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); ++i) {
				System.out.format("\033[4m%15s|\033[0m", metaData.getColumnName(i));
			}
			System.out.println();

			while (resultSet.next()) {
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i) {
					System.out.format("%15s|", resultSet.getString(i));
				}
				System.out.println();
			}
			System.out.println();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int updateProduct(int pid, String model) {
		int affectedRows = 0;
		try {
			updateProduct.setString(1, "model");
			updateProduct.setString(2, model);
			updateProduct.setInt(3, pid);

			affectedRows = updateProduct.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return affectedRows;
	}

	public int updateProduct(int pid, LocalDate date) {
		int affectedRows = 0;
		try {
			updateProduct.setString(1, "date");
			updateProduct.setDate(2, Date.valueOf(date));
			updateProduct.setInt(3, pid);

			affectedRows = updateProduct.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return affectedRows;
	}

	public int deleteProduct(int pid) {
		int affectedRows = 0;
		try {
			deleteProduct.setInt(1, pid);

			affectedRows = deleteProduct.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return affectedRows;
	}

	
}
