package thn.vn.web.miav.shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import thn.vn.web.miav.shop.services.StorageService;

import javax.annotation.Resource;

@SpringBootApplication
public class ShopApplication implements CommandLineRunner {
    @Resource
    StorageService storageService;
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }
}
