package victor.clean.lambdas;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;

class Movie {
	enum Type {
		REGULAR(MoviePriceService::computeRegularPrice), 
		NEW_RELEASE(MoviePriceService::computeNewReleasePrice), 
		CHILDREN(MoviePriceService::computeChildrenPrice);
		public final BiFunction<MoviePriceService, Integer, Integer> priceAlgo;

		private Type(BiFunction<MoviePriceService, Integer, Integer> priceAlgo) {
			this.priceAlgo = priceAlgo;
		}
		
	}

	private final Type type;

	public Movie(Type type) {
		this.type = type;
	}
	
}

//repo
interface TwoFactorRepo {
	int getFactor();
}

class MoviePriceService {
	@Autowired
	private TwoFactorRepo factorRepo;

	public MoviePriceService(TwoFactorRepo factorRepo) {
		this.factorRepo = factorRepo;
	}

	public int computePrice(Movie.Type type, int days)
	{
		return type.priceAlgo.apply(this, days);
	}

	public int computeChildrenPrice(int days) {
		return 5;
	}

	public int computeNewReleasePrice(int days) {
		return days * factorRepo.getFactor();
	}

	public int computeRegularPrice(int days) {
		return days + 1;
	}
}

public class E__TypeSpecific_Functionality {
	public static void main(String[] args) {
		TwoFactorRepo repo = mock(TwoFactorRepo.class);
		when(repo.getFactor()).thenReturn(2);
		MoviePriceService s = new MoviePriceService(repo);
		
		System.out.println(s.computePrice(Movie.Type.REGULAR,2));
		System.out.println(s.computePrice(Movie.Type.NEW_RELEASE,2));
		System.out.println(s.computePrice(Movie.Type.CHILDREN,2));
		System.out.println("COMMIT now!");
	}
}
