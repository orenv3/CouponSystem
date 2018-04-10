package general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import beans.coupon.system.Company;
import beans.coupon.system.Coupon;
import beans.coupon.system.CouponDateComperator;
import beans.coupon.system.CouponPriceComparator;
import beans.coupon.system.Customer;
import connectionPool.ConnectionPool;
import dao.db.units.CompanyDB_DAO;
import dao.db.units.CouponDB_DAO;
import dao.db.units.CustomerDB_DAO;
import myExceptions.MyCompanyExceptions;
import myExceptions.MyCouponException;
import myExceptions.MyCustomerException;

/**
 * This class contains static methods that assist methods of query and other,
 * that the system uses often.
 * 
 * @author oren Vinogura
 *
 */
public class StaticQueries {

	/**
	 * The method receives an empty list and adds in it objects of coupons. This
	 * method is only intended to serve other methods that uses ResultSet. This
	 * in case the other methods need to execute query commands in the database
	 * to creates list.
	 * 
	 * @param rst
	 *            This is a given java.sql.ResultSet. The father method provides
	 *            a java.sql.ResultSet, in order to implement query commands on
	 *            DB server.
	 * @param list
	 *            This is empty Collection list. The method provides a result of
	 *            a list containing coupon objects according to the requirement
	 *            in the SQL command.
	 */
	public static void couponResultSet(ResultSet rst, Collection<Coupon> list) {
		try {
			while (rst.next()) {
				Coupon c = new Coupon();
				c.setId(rst.getLong("ID"));
				c.setTitle(rst.getString("TITLE"));
				c.setStartDate(StaticQueries.convert2JavaDate(rst.getDate("START_DATE")));
				c.setEndDate(StaticQueries.convert2JavaDate(rst.getDate("END_DATE")));
				c.setAmount(rst.getInt("AMOUNT"));
				c.setType(CouponType.String2EnumType(rst.getString("TYPE")));
				c.setMessage(rst.getString("MESSAGE"));
				c.setPrice(rst.getFloat("PRICE"));
				c.setImage(rst.getString("IMAGE"));
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * converting java.sql.Date in to java.util.Date
	 * 
	 * @param date
	 * @return java.util.Date
	 */
	public static java.util.Date convert2JavaDate(java.sql.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		// convert the date to milliseconds
		java.util.Date current = cal.getTime();
		return current;
	}

	/**
	 * converting java.util.Date in to java.sql.Date
	 * 
	 * @param date
	 * @return java.sql.Date
	 */
	public static Date convert2SQL(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// convert the date to milliseconds
		java.util.Date current = cal.getTime();

		// create SQL date
		java.sql.Date thisDate = new java.sql.Date(current.getTime());
		return thisDate;
	}

	/**
	 * This method return a list of Customer or Company (objects) from the join
	 * tables. This method searching for The object according to the coupon ID.
	 * 
	 * @param tableName
	 *            This is a JoinTables ENUM. It can only be a name of the join
	 *            table.
	 * @param coupon
	 *            This is a coupon object. The method sql command is according
	 *            to this coupon's id.
	 * 
	 * @return Collection list of Customer OR Company objects.
	 */
	public static Collection getObjectsFromJoin(JoinTables tableName, Coupon coupon) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		Statement statment = null;
		Collection list = new LinkedList();
		con = pool.getConnection();
		String sql = "";

		switch (tableName) {
		case CUSTOMER_COUPON:
			CustomerDB_DAO custDAO = new CustomerDB_DAO();
			sql = "SELECT ID, CUST_NAME, PASSWORD FROM CUSTOMER_COUPON RIGHT JOIN Customer "
					+ "ON CUSTOMER_COUPON.CUST_ID = Customer.ID WHERE COUPON_ID = " + coupon.getId();
			try {
				statment = con.createStatement();
				ResultSet resultS = statment.executeQuery(sql);
				custDAO.customerResultSet(resultS, list);
				statment.close();
				if (list.size() == 0)
					throw new MyCustomerException("Some of the values are maybe wrong, no customer was found.");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				pool.returnConnection(con);
			}
			break;
		case COMPANY_COUPON:
			CompanyDB_DAO compDAO = new CompanyDB_DAO();
			sql = "SELECT ID, COMP_NAME, PASSWORD, EMAIL FROM " + tableName + "  RIGHT JOIN Company ON " + tableName
					+ ".COMP_ID = Company.ID WHERE COUPON_ID = " + coupon.getId();
			try {
				statment = con.createStatement();
				ResultSet resultS = statment.executeQuery(sql);
				compDAO.companyResultSet(resultS, list);
				statment.close();
				if (list.size() == 0)
					throw new MyCompanyExceptions("Some of the values are maybe wrong, no company was found.");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				pool.returnConnection(con);
			}
			break;
		default:
			break;
		}

		return list;
	}

	/**
	 * This method checkes if the name(or a title) is in the database before
	 * critical actions like creating or updating Objects in the DB. Prior to
	 * those critical actions on the DB, the name check is a must. In case of
	 * creating a new object, the new object's name must not be exist in the
	 * DB.In case of updating is just the opposite, the name must be exist on
	 * the DB.
	 * 
	 * @param units
	 *            This is a Clients ENUM. This parameter can be ONLY a name that
	 *            equals to a table name on DB - Customer, Company or Coupon (
	 *            table you need to examines).
	 * @param obj
	 *            This is the Object that we want to update\create on DB. In
	 *            other words, this is the object that the method checkes if his
	 *            name in the database.
	 * @param action
	 *            This is boolean action, true for create, false for update.
	 * 
	 * @return true, if the check completed successfuly. Otherwise the method
	 *         throws EXCEPTIONS. This method DOES NOT return false! If there is
	 *         either a duplication(for a creation) or the name no exist(for
	 *         update), the method throws EXCEPTION.
	 */
	public static boolean checkNameByUnits(Clients units, Object obj, boolean action) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		ResultSet rst = null;
		con = pool.getConnection();
		String sql = "";

		switch (units) {// ********* every case divided to if action is true or false *********
		case COMPANY:
			if (obj instanceof Company) {
				Company c4company = (Company) obj;
				Company tmpComp = new Company();
				sql = "SELECT * FROM company WHERE COMP_NAME=?";
				CompanyDB_DAO compDAO = new CompanyDB_DAO();
				if (action == false) { // *************** if action false *************** 
					tmpComp = compDAO.read(c4company.getId());
					if (!(tmpComp.getName().equals(c4company.getName()))) {
						pool.returnConnection(con);
						throw new MyCompanyExceptions("There is mismatch between the company name and the company id.\n"
								+ "Company id and name are uniqe.\nPlease contact the helpdesk!");
					}
				} else if (action == true) {// *************** if action true ************** 
					try (PreparedStatement statmnt = con.prepareStatement(sql);) {
						statmnt.setString(1, c4company.getName());
						rst = statmnt.executeQuery();
						if (rst.next()) {
							//if it reach this point = there is a line in the DB --> the name already exist
							throw new MyCompanyExceptions("This company name already exist in the system");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						try {
							if (action == true)
								rst.close();
							pool.returnConnection(con);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else
				throw new MyCompanyExceptions("This is not Company object");
			break;

		case COUPON:
			if (obj instanceof Coupon) {
				Coupon c4Coupon = (Coupon) obj;
				Coupon tmpCoupon = new Coupon();
				sql = "SELECT * FROM coupon WHERE TITLE=?";
				CouponDB_DAO CouponDAO = new CouponDB_DAO();
				if (action == false) {// *************** if action false *********** 
					tmpCoupon = CouponDAO.read(c4Coupon.getId());
					if (!(tmpCoupon.getTitle().equals(c4Coupon.getTitle()))) {
						pool.returnConnection(con);
						throw new MyCouponException("There is mismatch between the coupon name and the coupon id.\n"
								+ "Coupon id and name are uniqe.\nPlease contact the helpdesk!");
					}
				} else if (action == true) {// *************** if action true *********** 
					try (PreparedStatement statmnt = con.prepareStatement(sql);) {
						statmnt.setString(1, c4Coupon.getTitle());
						rst = statmnt.executeQuery();
						if (rst.next()) {
							//if it reach this point = there is a line in the DB --> the name already exist
							throw new MyCouponException("This coupon name already exist in the system");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {

						try {
							if (action == true)
								rst.close();
							pool.returnConnection(con);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else
				throw new MyCouponException("This is not Coupon object.");
			break;
		case CUSTOMER:
			if (obj instanceof Customer) {
				Customer c4Customer = (Customer) obj;
				Customer tmpCust = new Customer();
				CustomerDB_DAO custDAO = new CustomerDB_DAO();
				sql = "SELECT * FROM customer WHERE CUST_NAME=?";
				if (action == false) {// *************** if action false *********** 
					tmpCust = custDAO.read(c4Customer.getId());
					if (!(tmpCust.getName().equals(c4Customer.getName()))) {
						pool.returnConnection(con);
						throw new MyCustomerException(
								"There is mismatch between the customer name and the customer id.\n"
										+ "Customer id and name are uniqe.\nPlease contact the helpdesk!");
					}
				} else if (action == true) {// *************** if action true *********** 
					try (PreparedStatement statmnt = con.prepareStatement(sql);) {
						statmnt.setString(1, c4Customer.getName());
						rst = statmnt.executeQuery();
						if (rst.next()) {
							//if it reach this point = there is a line in the DB --> the name already exist
							throw new MyCustomerException("This customer name already exist in the system");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						try {
							if (action == true)
								rst.close();
							pool.returnConnection(con);
						} catch (SQLException e) {
							e.printStackTrace();
						}

					}
				}
			} else
				throw new MyCustomerException("This is not customer object");
			break;
		default:
			break;
		}
		try {
			if (action == true)
				rst.close();
			if (action == false)
				pool.returnConnection(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * This method changes parameters in the join tables (either customer_coupon
	 * OR company_coupon). This method can either create or delete lines
	 * according to object's id parameters.
	 * 
	 * 
	 * NOTE: If the action is create then this method throws EXCEPTION if the
	 * amount of the coupon is 0.
	 * 
	 * @param tableName
	 *            This is a JoinTables ENUM. It can only be a name of a join
	 *            table. The method is divided according to those ENUM names and
	 *            examines the relevant table(put here the table you need to
	 *            examines).
	 * @param obj
	 *            This is either Customer OR Company (Object) to update OR
	 *            delete them and their coupon on DB.
	 * @param coupon
	 *            This is the coupon object to update\delete.
	 * @param action
	 *            Boolean action, if action is true the method will create the
	 *            coupon and the object in the join table that defined in the
	 *            parameters. Otherwise, the method will delete the correct line
	 *            from the join table. The action is perform according the
	 *            coupon's id and the object's id.
	 */
	public static void Obj_Action_JoinTable(JoinTables tableName, Object obj, Coupon coupon, boolean action) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement statment = null;
		Company company = null;
		Customer customer = null;
		con = pool.getConnection();
		String sql = "";


		if (action) {
			sql = "INSERT INTO " + tableName + " VALUES (?,?)";

		} else {
			sql = "DELETE FROM " + tableName + " WHERE " + tableName.getColume() + " = ? AND COUPON_ID=?";
		}


		switch (tableName) {
		case COMPANY_COUPON:
			if (obj instanceof Company) {
				company = (Company) obj;

				if ((StaticQueries.checkCouponObjAmount(coupon) && action == true) || action == false) {
					try {
						statment = con.prepareStatement(sql);
						statment.setLong(1, company.getId());
						statment.setLong(2, coupon.getId());
						statment.executeUpdate();

					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						try {
							statment.close();
							pool.returnConnection(con);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					break;
				} else {
					pool.returnConnection(con);
					if (action == true)
						throw new MyCouponException("The amount of the coupon must be bigger then 0!");
				}
			} else
				throw new MyCompanyExceptions("This is not a Company object");
		case CUSTOMER_COUPON:
			if (obj instanceof Customer) {
				customer = (Customer) obj;

				if ((StaticQueries.checkCouponObjAmount(coupon) && action == true) || action == false) {
					try {
						statment = con.prepareStatement(sql);
						statment.setLong(1, customer.getId());
						statment.setLong(2, coupon.getId());
						statment.executeUpdate();

					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						try {
							statment.close();
							pool.returnConnection(con);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					break;

				} else {
					pool.returnConnection(con);
					if (action == true)
						throw new MyCustomerException("The amount of the coupon is 0. purchase this coupon is invalid."
								+ "\nPlease contact the helpdesk.");
				}
			} else
				throw new MyCustomerException("This is not a Customer object");
		default:
			break;
		}
	}

	/**
	 * This method getting a list of coupons (objects) and delete their lines
	 * from the relevant join table (either customer_coupon OR company_coupon).
	 * This action performs according to the coupon's id. it follows that, if
	 * the action is on company_coupon table it can delete only one line at the
	 * time. However, if the action on the customer_coupon table it can delete a
	 * lot in one time(few customer can use single coupon's id).
	 * 
	 * @param tableName
	 *            This is a JoinTables ENUM. It can only be a name of the join
	 *            table.
	 * @param couponList
	 *            Collection<Coupon> list of coupon objects for a deletion.
	 */
	public static void deleteListFromJoin(JoinTables tableName, Collection<Coupon> couponList) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "DELETE FROM " + tableName + " WHERE COUPON_ID =?";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			for (Coupon coupon : couponList) {
				statmnt.setLong(1, coupon.getId());
				statmnt.executeUpdate();
			}
		} catch (MyCouponException e) {
			throw new MyCouponException("There is no such coupon.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * This method checks the amount of the coupon, whethere the amount of the
	 * coupon is zero(invalid) or graeter then zero.
	 * 
	 * @param coupon
	 *            A coupon object to check the amount.
	 * @return True if the amount is bigger from zero. Otherwise return false.
	 */
	public static boolean checkCouponObjAmount(Coupon coupon) {
		if (coupon.getAmount() <= 0)
			return false;
		return true;
	}

	/**
	 * This method generates a sub list from the given list. The list that the
	 * method provides is a sorted list of coupons ordered by coupons's end
	 * date. The list will end when it reach the max end date that given. The
	 * sort of the given list is via beans.CouponDateComperator, Date: old-new.
	 * 
	 * @param maxDate
	 *            this is the farthest date that the list will reach.
	 * @return Collection<Coupon>list sorted by date till the given date.
	 */
	public static Collection<Coupon> getCouponTillMaxDate(Date maxDate, Collection<Coupon> couponList) {
		LinkedList<Coupon> sortList = (LinkedList<Coupon>) couponList;
		Collections.sort(sortList, new CouponDateComperator());
		LinkedList<Coupon> listTillMax = new LinkedList<>();
		for (Coupon coupon : sortList) {
			if (coupon.getEndDate().before(maxDate) || coupon.getEndDate().equals(maxDate))
				listTillMax.add(coupon);
		}
		return listTillMax;

	}

	/**
	 * This method generates a sub list from the given list. The list that the
	 * method provides is a sorted list of coupons ordered by coupons's price.
	 * The list will end when it reach the max price that given. The sort of the
	 * given list is via beans.CouponPriceComparator, Price: low-high.
	 * 
	 * @param maxPrice
	 *            this is the maximum price the list will reach.
	 * @return Collection list of coupons sorted by price till the given max
	 *         price.
	 */
	public static LinkedList<Coupon> getCouponsByMaxPrice(double maxPrice, Collection<Coupon> list) {
		LinkedList<Coupon> listByPrice = (LinkedList<Coupon>) list;
		Collections.sort(listByPrice, new CouponPriceComparator());
		LinkedList<Coupon> listTillMax = new LinkedList<>();
		for (Coupon coupon : listByPrice) {
			if (coupon.getPrice() <= maxPrice)
				listTillMax.add(coupon);
		}
		if (listTillMax.size() == 0)
			throw new MyCouponException("The price is too low. There is no coupon with this price.");
		else
			return listTillMax;
	}

}
