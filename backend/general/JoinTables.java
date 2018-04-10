package general;

/**
 * This is an ENUM of both join tables name. In addition, the constractor
 * configured with one colume name that makes a difference between them.
 * 
 * @author oren vinogura
 *
 */
public enum JoinTables {
	COMPANY_COUPON("COMP_ID"), CUSTOMER_COUPON("CUST_ID");

	private String colume;

	private JoinTables() {
	}

	private JoinTables(String colume) {
		this.colume = colume;
	}

	/**
	 * The method returns the one colume name that makes a difference between
	 * join tables.
	 * 
	 * @return the one colume name that makes a difference between join tables.
	 */
	public String getColume() {
		return colume;
	}

}
