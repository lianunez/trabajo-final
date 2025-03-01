package org.data.repository.seeder;

import jakarta.persistence.PersistenceException;
import org.data.repository.product.ProductRepository;
import org.data.repository.provider.ProviderRepository;
import org.data.repository.user.UserRepository;
import org.domain.product.Product;
import org.domain.provider.Provider;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataSeeder(ProductRepository productRepository, ProviderRepository providerRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            List<Product> products = productRepository.getProducts();

            if (!(products.size() > 0)) {
                // provider persists
                Provider p1 = Provider.builder()
                        .cuit("5000")
                        .name("PROVIDER_1")
                        .build();
                providerRepository.createProvider(p1);

                Provider p2 = Provider.builder()
                        .cuit("6000")
                        .name("PROVIDER_2")
                        .build();
                providerRepository.createProvider(p2);

                Provider p3 = Provider.builder()
                        .cuit("7000")
                        .name("PROVIDER_3")
                        .build();
                providerRepository.createProvider(p3);

                // user persists
                User u1 = User.builder()
                        .userName("USER_1")
                        .password("")
                        .build();
                userRepository.createUser(u1);

                User u2 = User.builder()
                        .userName("USER_2")
                        .password("")
                        .build();
                userRepository.createUser(u2);

                User u3 = User.builder()
                        .userName("USER_3")
                        .password("")
                        .build();
                userRepository.createUser(u3);

                // provider and user search. Reason: use their ids to create new products
                Provider provider1 = providerRepository.findProvider("5000");
                Provider provider2 = providerRepository.findProvider("6000");
                Provider provider3 = providerRepository.findProvider("7000");
                User user1 = userRepository.findUser("USER_1");
                User user2 = userRepository.findUser("USER_2");
                User user3 = userRepository.findUser("USER_3");

                // products persists
                // provider 1
                Product prod1 = Product.builder()
                        .code("0100")
                        .amount(10)
                        .name("MEDICINE_1")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user1)
                        .provider(provider1)
                        .build();
                productRepository.createProduct(prod1);

                Product prod2 = Product.builder()
                        .code("0101")
                        .amount(10)
                        .name("MEDICINE_2")
                        .expiryDate(expiryDate(3))
                        .date(createdAt(2))
                        .user(user1)
                        .provider(provider1)
                        .build();
                productRepository.createProduct(prod2);

                Product prod3 = Product.builder()
                        .code("0110")
                        .amount(10)
                        .name("MEDICINE_3")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(2))
                        .user(user1)
                        .provider(provider1)
                        .build();
                productRepository.createProduct(prod3);

                Product prod4 = Product.builder()
                        .code("0111")
                        .amount(10)
                        .name("MEDICINE_4")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(3))
                        .user(user1)
                        .provider(provider1)
                        .build();
                productRepository.createProduct(prod4);

                Product prod5 = Product.builder()
                        .code("1000")
                        .amount(10)
                        .name("MEDICINE_5")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(3))
                        .user(user3)
                        .provider(provider1)
                        .build();
                productRepository.createProduct(prod5);

                // provider 2
                Product prod6 = Product.builder()
                        .code("5055")
                        .amount(10)
                        .name("MEDICINE_1")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user1)
                        .provider(provider2)
                        .build();
                productRepository.createProduct(prod6);

                Product prod7 = Product.builder()
                        .code("5050")
                        .amount(10)
                        .name("MEDICINE_2")
                        .expiryDate(expiryDate(3))
                        .date(createdAt(2))
                        .user(user2)
                        .provider(provider2)
                        .build();
                productRepository.createProduct(prod7);

                Product prod8 = Product.builder()
                        .code("5005")
                        .amount(10)
                        .name("MEDICINE_3")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(2))
                        .user(user2)
                        .provider(provider2)
                        .build();
                productRepository.createProduct(prod8);

                Product prod9 = Product.builder()
                        .code("5000")
                        .amount(10)
                        .name("MEDICINE_4")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(3))
                        .user(user3)
                        .provider(provider2)
                        .build();
                productRepository.createProduct(prod9);

                Product prod10 = Product.builder()
                        .code("4440")
                        .amount(10)
                        .name("MEDICINE_5")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(3))
                        .user(user1)
                        .provider(provider2)
                        .build();
                productRepository.createProduct(prod10);

                // provider 3
                Product prod11 = Product.builder()
                        .code("4044")
                        .amount(10)
                        .name("MEDICINE_1")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user1)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod11);

                Product prod12 = Product.builder()
                        .code("4040")
                        .amount(10)
                        .name("MEDICINE_2")
                        .expiryDate(expiryDate(3))
                        .date(createdAt(2))
                        .user(user2)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod12);

                Product prod13 = Product.builder()
                        .code("4004")
                        .amount(10)
                        .name("MEDICINE_3")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user2)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod13);

                Product prod14 = Product.builder()
                        .code("4000")
                        .amount(10)
                        .name("MEDICINE_4")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user3)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod14);

                Product prod15 = Product.builder()
                        .code("3000")
                        .amount(10)
                        .name("MEDICINE_5")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(3))
                        .user(user1)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod15);

                Product prod16 = Product.builder()
                        .code("3033")
                        .amount(10)
                        .name("MEDICINE_1")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user1)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod16);

                Product prod17 = Product.builder()
                        .code("3003")
                        .amount(10)
                        .name("MEDICINE_2")
                        .expiryDate(expiryDate(3))
                        .date(createdAt(2))
                        .user(user2)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod17);

                Product prod18 = Product.builder()
                        .code("3303")
                        .amount(10)
                        .name("MEDICINE_3")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user2)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod18);

                Product prod19 = Product.builder()
                        .code("3330")
                        .amount(10)
                        .name("MEDICINE_4")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(1))
                        .user(user3)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod19);

                Product prod20 = Product.builder()
                        .code("3333")
                        .amount(10)
                        .name("MEDICINE_5")
                        .expiryDate(expiryDate(2))
                        .date(createdAt(3))
                        .user(user1)
                        .provider(provider3)
                        .build();
                productRepository.createProduct(prod20);
            }

        } catch (PersistenceException e) {
            throw new PersistenceException("Data seeder: Error trying save initial data", e);
        }
    }

    private String createdAt(int range) {
        return LocalDate.now()
                .minusMonths(range)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String expiryDate(int range) {
        return LocalDate.now()
                .plusMonths(range)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
