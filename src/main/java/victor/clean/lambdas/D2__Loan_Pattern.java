package victor.clean.lambdas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.SneakyThrows;

// export all orders to a file

interface OrderRepo extends JpaRepository<Order, Long> { // Spring Data FanClub
	Stream<Order> findByActiveTrue(); // 1 Mln orders ;)
}
class OrderExporter {
	private final static Logger log = LoggerFactory.getLogger(OrderExporter.class);

	public static void main(String[] args) throws Exception {
		OrderExporter exporter = new OrderExporter(); // ne prefacem ca ne vine de la Spring. @Autopwired
		OrderContent content = new OrderContent();
		
		exporter.exportFile("users.csv", content::writeUserExportContent);
		exporter.exportFile("users.csv", content::writeOrderExportContent);
	}
	
	public File exportFile(String fileName, Consumer<Writer> contentWriter) throws Exception {
		File file = new File("export/" + fileName);
		try (Writer writer = new FileWriter(file)) {
			contentWriter.accept(writer);
			return file;
		} catch (Exception e) {
			// TODO send email notification
			log.debug("Gotcha!", e); // TERROR-Driven Development
			throw e;
		}
	}

}
class OrderContent {

	private OrderRepo repo;
	public void writeUserExportContent(Writer writer) {
		try {
			writer.write("username;lastname\n");
			userRepo.findAll()
				.stream()
				.map(u -> u.getUsername() + ";" + u.getLastName())
				.forEach(Unchecked.consumer(writer::write));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private UserRepo userRepo;
	@SneakyThrows
	public void writeOrderExportContent(Writer writer)  {
		writer.write("OrderID;Date\n");
		repo.findByActiveTrue()
			.map(o -> o.getId() + ";" + o.getCreationDate())
			.forEach(Unchecked.consumer(writer::write));
	}
	
}


