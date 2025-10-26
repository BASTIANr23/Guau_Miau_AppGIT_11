package cl.duoc.guaumiau.data.repository

import cl.duoc.guaumiau.GuauMiauApplication
import cl.duoc.guaumiau.data.local.ProductDao
import cl.duoc.guaumiau.data.model.PetType
import cl.duoc.guaumiau.data.model.Product
import cl.duoc.guaumiau.data.model.ProductCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProductRepository {
    
    private val productDao: ProductDao = GuauMiauApplication.instance.database.productDao()
    
    private val sampleProducts = listOf(
        Product(
            id = 1,
            name = "Pelota Interactiva LED",
            description = "Pelota con luces LED que se activa con el movimiento. Perfecta para mantener a tu perro entretenido durante horas. Material resistente y seguro.",
            price = 15990.0,
            category = ProductCategory.DOG_TOYS,
            petType = PetType.DOG,
            imageUrl = "https://placeholder.com/300x300?text=Pelota+LED",
            isInnovative = true,
            stock = 15
        ),
        Product(
            id = 2,
            name = "Ratón de Catnip Orgánico",
            description = "Juguete relleno de catnip 100% orgánico. Estimula el juego natural de tu gato. Hecho con materiales sostenibles y biodegradables.",
            price = 8990.0,
            category = ProductCategory.CAT_TOYS,
            petType = PetType.CAT,
            imageUrl = "https://placeholder.com/300x300?text=Raton+Catnip",
            isSustainable = true,
            stock = 25
        ),
        Product(
            id = 3,
            name = "Columpio con Espejo",
            description = "Columpio para aves con espejo integrado. Estimula la actividad física y mental. Fabricado con materiales no tóxicos.",
            price = 12990.0,
            category = ProductCategory.BIRD_TOYS,
            petType = PetType.BIRD,
            imageUrl = "https://placeholder.com/300x300?text=Columpio+Ave",
            stock = 10
        ),
        Product(
            id = 4,
            name = "Dispensador de Premios Inteligente",
            description = "Juguete interactivo que dispensa premios cuando tu mascota resuelve el puzzle. Ajustable en dificultad. Compatible con perros y gatos.",
            price = 24990.0,
            category = ProductCategory.INTERACTIVE,
            petType = PetType.DOG,
            imageUrl = "https://placeholder.com/300x300?text=Dispensador",
            isInnovative = true,
            stock = 8
        ),
        Product(
            id = 5,
            name = "Cuerda de Algodón Reciclado",
            description = "Cuerda para jugar hecha 100% de algodón reciclado. Resistente y ecológica. Ideal para juegos de tira y afloja.",
            price = 6990.0,
            category = ProductCategory.DOG_TOYS,
            petType = PetType.DOG,
            imageUrl = "https://placeholder.com/300x300?text=Cuerda",
            isSustainable = true,
            stock = 30
        ),
        Product(
            id = 6,
            name = "Túnel Plegable para Gatos",
            description = "Túnel de juego plegable con múltiples entradas. Incluye bola colgante. Fácil de guardar y transportar.",
            price = 18990.0,
            category = ProductCategory.CAT_TOYS,
            petType = PetType.CAT,
            imageUrl = "https://placeholder.com/300x300?text=Tunel+Gato",
            stock = 12
        ),
        Product(
            id = 7,
            name = "Escalera de Bambú para Aves",
            description = "Escalera natural de bambú sostenible. Perfecta para el ejercicio de aves pequeñas y medianas. Fácil instalación.",
            price = 9990.0,
            category = ProductCategory.BIRD_TOYS,
            petType = PetType.BIRD,
            imageUrl = "https://placeholder.com/300x300?text=Escalera",
            isSustainable = true,
            stock = 18
        ),
        Product(
            id = 8,
            name = "Pelota Sonora Automática",
            description = "Pelota que se mueve sola y emite sonidos. Sensor de movimiento inteligente. Recargable vía USB. Ideal para gatos curiosos.",
            price = 21990.0,
            category = ProductCategory.INTERACTIVE,
            petType = PetType.CAT,
            imageUrl = "https://placeholder.com/300x300?text=Pelota+Auto",
            isInnovative = true,
            stock = 7
        ),
        Product(
            id = 9,
            name = "Kong Clásico Resistente",
            description = "El clásico juguete Kong de caucho natural ultra resistente. Perfecto para rellenar con premios. Disponible en varios tamaños.",
            price = 13990.0,
            category = ProductCategory.DOG_TOYS,
            petType = PetType.DOG,
            imageUrl = "https://placeholder.com/300x300?text=Kong",
            stock = 20
        ),
        Product(
            id = 10,
            name = "Varita Mágica con Plumas",
            description = "Varita interactiva con plumas naturales. Estimula el instinto cazador de tu gato. Mango ergonómico antideslizante.",
            price = 7990.0,
            category = ProductCategory.CAT_TOYS,
            petType = PetType.CAT,
            imageUrl = "https://placeholder.com/300x300?text=Varita",
            stock = 22
        )
    )
    
    suspend fun initializeProducts() {
        // Verificar si ya hay productos en la base de datos
        val existingProducts = productDao.getAllProducts().first()
        if (existingProducts.isEmpty()) {
            // Si no hay productos, insertar los productos de muestra
            productDao.insertProducts(sampleProducts)
        }
    }
    
    fun getAllProductsFlow(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }
    
    suspend fun getAllProducts(): List<Product> {
        delay(500)
        return productDao.getAllProducts().first()
    }
    
    suspend fun getProductsByCategory(category: ProductCategory): List<Product> {
        delay(500)
        val allProducts = productDao.getAllProducts().first()
        return if (category == ProductCategory.ALL) {
            allProducts
        } else {
            allProducts.filter { it.category == category }
        }
    }
    
    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }
    
    suspend fun searchProducts(query: String): List<Product> {
        delay(500)
        val allProducts = productDao.getAllProducts().first()
        return allProducts.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true)
        }
    }
    
    fun getProductsByPetType(petType: PetType): Flow<List<Product>> {
        return productDao.getProductsByPetType(petType.name)
    }
    
    suspend fun addProduct(product: Product): Result<Long> {
        return try {
            val productId = productDao.insertProduct(product)
            Result.success(productId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            productDao.updateProduct(product)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
