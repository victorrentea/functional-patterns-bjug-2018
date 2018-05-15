package victor.clean.lambdas;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Data;

@SuppressWarnings("deprecation")
public class Puzzlers {

	
	public static void main(String[] args) {
		new Puzzlers().go();
	}
	public void go() {
		List<City> cities = asList(
				new City("Cluj", 320_000),
				new City("Iasi", 290_000),
				new City("Bucuresti", 1_800_000)				
				);
		
		System.out.println("========= Puzle 1 =========");
		System.out.println(cities.stream()
			.filter(c -> c.getPopulation() > 300_000)
			.map(City::getName)
			.collect(toList()));
		// 1) [org..City@deadbeef, org..City@141131]
		// 2) [Cluj, Bucuresti]
		// 3) [Cluj=org..City@deadbeef, Bucuresti=org..City@141131]
		
		
		System.out.println("========= Puzle 2 =========");
		System.out.println("Result: " + cities.stream()
			.filter(city -> {
				System.out.println("Pop " + city.getName());
				return city.getPopulation() > 300_000;
			})
			.filter(city -> {
				System.out.println("Name " + city.getName());
				return city.getName().length() >= 4;
			})
			.map(City::getName)
			.collect(joining(",")));
		// 1) Pop Cluj > Name Cluj > Pop Iasi > Pop Bucuresti > Name Bucuresti > Result: Cluj,Bucuresti
		// 2) Pop Cluj > Pop Iasi > Pop Bucuresti > Name Cluj > Name Iasi > Name Bucuresti > Result: Cluj, Bucuresti
		// 3) Pop Cluj > Name Cluj > Pop Iasi > Name Iasi > Pop Bucuresti > Name Bucuresti > Result: Cluj, Bucuresti
		
		
		System.out.println("========= Puzle 3 =========");
		cities.stream()
			.peek(c -> System.out.println("See " + c.getName().charAt(0)))
			.sorted(Comparator.comparing(City::getPopulation).reversed())
			.map(c -> {
				System.out.println("Get " + c.getName().charAt(0));
				return c.getName().charAt(0);
			})
			.forEach(System.out::println);
		//1) See B > Get B > B > See C > Get C > C
		//2) See C > See I > See B > Get B > B > Get C > C > Get I > I
		//3) See B > See I > See B > Get B > Get C > Get I > B > C > I
		
		try {
		
			System.out.println("========= Puzle 4 =========");
			Stream<String> stream = Stream.of("A", "B", "C");
			stream.forEach(System.out::println);
			stream.forEach(System.out::println);
			// 1) A > B > C > A > B > C
			// 2) A > B > C > IllegalStateException
			// 3) A > B > C
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		System.out.println("========= Puzle 5 =========");
		List<Country> countries = asList(
				new Country("Romania", cities),
				new Country("Bulgaria", asList(new City("Sofia", 1_300_000))));
		System.out.println(countries.stream()
				.flatMap(country -> country.getCities().stream())
				.map(City::getName)
				.sorted()
				.collect(toSet()));
		// 1) [Cluj, Sofia]
		// 2) [Bucuresti, Cluj, Iasi, Sofia]
		// 3) None of the above
		
		
		System.out.println("========= Puzle 6 =========");
		System.out.println(cities.stream()
			.max((a,b) -> a.getPopulation() - b.getPopulation())
			.map(City::getName));
		// What can we write here , for this code to print "Optional[Bucuresti]"
		// 1) (City a, City b) -> {return a.getPopulation() - b.getPopulation();}
		// 2) (a,b) -> a.getPopulation() - b.getPopulation()
		// 3) Comparator.comparing(city -> city.getPopulation())
		// 4) Comparator.comparing(City::getPopulation)
		// 5) Impossible. It will never print Optional[Bucuresti]
		
		
		System.out.println("========= Puzle 7 =========");
		int n = 0;
//		cities.forEach(city -> { n ++; }); 
		System.out.println("Total cities : " + n);
		// Will this work ?
		
		System.out.println("========= Puzle 8 =========");
		System.out.println(Optional.of("20180131")
				.map(this::parseDateIfPossible)
				.map(Date::getYear));
		// 1) RuntimeException
		// 2) Optional.empty
		// 3) Optional[118] because 2018 = 1900 + .getYear() 
		
		
		System.out.println("========= Puzle 9 =========");
		String luc = ""
				+ "Porni luceafarul. Cresteau\n"
				+ "In cer a lui aripe,\n"
				+ "Si cai de mii de ani treceau\n"
				+ "In tot atatea clipe. [...]\n"
				+ "\n"
				+ "Caci unde-ajunge nu-i hotar,\n"
				+ "Nici ochi spre a cunoaste,\n"
				+ "Si vremea-ncearca in zadar\n"
				+ "Din goluri a se naste. [...]\n"
				+ "\n"
				+ "Reia-mi al nemuririi nimb\n"
				+ "Si focul din privire,\n"
				+ "Si pentru toate da-mi in schimb\n"
				+ "O ora de iubire...\n";
		
		System.out.println(
				Stream.of(luc.replaceAll("\\W+", " ").split(" "))
					.distinct()
					.collect(groupingBy(String::length, counting()))
				);
		// 1) {1=5, 2=17, 3=9, 4=7, 5=8, 6=7, 7=3, 8=2, 9=1, 10=1}
		// 2) {1=3, 2=7, 3=8, 4=3, 5=4, 6=5, 7=1, 8=1, 9=3, 10=3, 11=5, 12=1, 13=1, 14=1}
		// 2) {1=3, 2=9, 3=9, 4=7, 5=8, 6=7, 7=3, 8=2, 9=1, 10=1}
	}
	
	private Date parseDateIfPossible(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d = sdf.parse(dateStr);
			if (!sdf.format(d).equals(dateStr)) {
				return null;
			}
			return d;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	
//		Runnable shareStack() { 
//			int n = 0;
//			return () -> {n ++;};
//		}
//		
//		{
//			Runnable r = shareStack();
//			r.run();
//			r.run();
//		}

	
	
	public boolean hasBigCities(Country country) {
		return country.getCities().stream()
				.anyMatch(this::isBigCity);
	}
	
	private boolean isBigCity(City city) {
		
		return true;
	}
}


@Data
class City {
	private final String name;
	private final int population;
}

@Data
class Country {
	private final String name;
	private final List<City> cities;
}
