package beans.coupon.system;

import java.util.Comparator;

/**
 * This class is Comparator for coupon, ordered by date. Date: old-new
 * 
 * @author oren vinogura
 *
 */
public class CouponDateComperator implements Comparator<Coupon> {

	@Override
	public int compare(Coupon c1, Coupon c2) {

		if (c1.getEndDate().before(c2.getEndDate()))
			return -1;
		else if (c1.getEndDate().after(c2.getEndDate()))
			return 1;
		else
			return 0;

	}

}
