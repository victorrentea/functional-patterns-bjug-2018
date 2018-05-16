package victor.clean.lambdas;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import lombok.Data;

/* "I call it my billion-dollar mistake. 
 * It was the invention of the null reference in 1965..."
 *  -- Sir Charles Antony Richard  */

// Get a discount line to print in UI


class DiscountService {
	public String getDiscountLine(Customer customer) {
		Optional<Integer> discount = getApplicableDiscountPercentage(customer.getMemberCard());
		return discount.map(d -> "Discount: " + d).orElse("");
	}
		
	private Optional<Integer> getApplicableDiscountPercentage(MemberCard card) { 
		if (card.getFidelityPoints() >= 100) {
			return of(5);
		}
		if (card.getFidelityPoints() >= 50) {
			return of(3);
		}
		return empty();
	}
		
	// test: 60, 10, no MemberCard
	public static void main(String[] args) {
		DiscountService service = new DiscountService();
		System.out.println(service.getDiscountLine(new Customer(new MemberCard(60))));
		System.out.println(service.getDiscountLine(new Customer(new MemberCard(10))));
	}
}







// VVVVVVVVV ==== supporting (dummy) code ==== VVVVVVVVV
class Customer {
	private MemberCard memberCard;
	public Customer() {
	}
	public Customer(MemberCard profile) {
		this.memberCard = profile;
	}
	public MemberCard getMemberCard() {
		return memberCard;
	}
}

@Data
class MemberCard {
	private final int fidelityPoints;
}
