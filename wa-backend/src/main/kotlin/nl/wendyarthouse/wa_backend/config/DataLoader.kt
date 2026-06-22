package nl.wendyarthouse.wa_backend.config

import nl.wendyarthouse.wa_backend.model.Product
import nl.wendyarthouse.wa_backend.repository.ProductRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.math.BigDecimal

@Configuration
class DataLoader {

    @Bean
    @Profile("!prod")
    fun loadTestData(productRepository: ProductRepository) = CommandLineRunner {
        if (productRepository.count() == 0L) {
            productRepository.saveAll(
                listOf(
                    Product(
                        name = "Klein schaaltje",
                        description = "Handgemaakt schaaltje van lokale klei. Perfect als snackschaaltje of voor sieraden. Doorsnede 12 cm.",
                        price = BigDecimal("14.95"),
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg",
                        category = "Schaaltjes",
                        stock = 25
                    ),
                    Product(
                        name = "Grote schaal",
                        description = "Ruime serveerschaal met een rustiek glazuur. Ideaal voor fruit of als middelpunt op tafel. Doorsnede 28 cm.",
                        price = BigDecimal("39.95"),
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg",
                        category = "Schaaltjes",
                        stock = 10
                    ),
                    Product(
                        name = "Keramische beker",
                        description = "Stevig en warmhoudend. Elke beker is uniek door het handmatige glazuurwerk. Inhoud 300 ml.",
                        price = BigDecimal("18.50"),
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg",
                        category = "Bekers",
                        stock = 30
                    ),
                    Product(
                        name = "Espressokopje",
                        description = "Klein kopje voor een sterke koffie. Handgevormd met een mat zwart glazuur. Inhoud 80 ml.",
                        price = BigDecimal("12.50"),
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg",
                        category = "Bekers",
                        stock = 20
                    ),
                    Product(
                        name = "Diepe kom",
                        description = "Diepe soep- of saladekom met een warme terracottakleur. Inhoud ca. 600 ml.",
                        price = BigDecimal("24.95"),
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg",
                        category = "Kommen",
                        stock = 15
                    ),
                    Product(
                        name = "Ontbijtbord",
                        description = "Plat bord met een licht verhoogde rand. Handgemaakt in een zacht saliegroen glazuur. Doorsnede 22 cm.",
                        price = BigDecimal("29.95"),
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg",
                        category = "Borden",
                        stock = 18
                    )
                )
            )
            println("✅ Testdata geladen: 6 voorbeeldproducten toegevoegd")
        }
    }
}