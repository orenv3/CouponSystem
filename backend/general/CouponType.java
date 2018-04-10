package general;

/**
 * This is an ENUM for Coupon's Types.
 * 
 * @author oren vinogura
 *
 */
public enum CouponType {
	FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING, ELECTRICITY, RESTURANS;

	/**
	 * This method converts the coupon's type (ENUM) to String in order to
	 * insert it to the DB.
	 * 
	 * @param CouponType
	 *            This is the coupon type of the coupon.
	 * @return String when String=ENUM, for example enum=FOOD then String=FOOD
	 *         in capital letters.
	 */
	public static String EnumType2String(CouponType type) {
		String s = "";
		switch (type) {
		case CAMPING:
			s += CAMPING;
			break;
		case ELECTRICITY:
			s += ELECTRICITY;
			break;
		case FOOD:
			s += FOOD;
			break;
		case HEALTH:
			s += HEALTH;
			break;
		case RESTURANS:
			s += RESTURANS;
			break;
		case SPORTS:
			s += SPORTS;
			break;
		case TRAVELLING:
			s += TRAVELLING;
			break;
		}

		return s;
	}

	/**
	 * This method convertes String to ENUM type. The String taken from the
	 * colume coupon.type on DB.
	 * 
	 * @param type
	 *            This is the String coupon type from the DB.
	 * @return CouponType Coupon Type ENUM when String=ENUM.
	 */
	public static CouponType String2EnumType(String type) {
		CouponType t = null;
		switch (type) {
		case "CAMPING":
			t = CAMPING;
			break;
		case "ELECTRICITY":
			t = ELECTRICITY;
			break;
		case "FOOD":
			t = FOOD;
			break;
		case "HEALTH":
			t = HEALTH;
			break;
		case "RESTURANS":
			t = RESTURANS;
			break;
		case "SPORTS":
			t = SPORTS;
			break;
		case "TRAVELLING":
			t = TRAVELLING;
			break;
		}

		return t;
	}

	/**
	 * This method provide one of the couponType (ENUM) randomly.
	 * 
	 * @return one of the couponType (ENUM) randomly
	 */
	public static CouponType rnd() {
		CouponType c[] = CouponType.values();
		return c[(int) (Math.random() * 4)];//it is only till 4 becouse of the class ShowTable - it makes better view on the tables in the consule
	}
}
