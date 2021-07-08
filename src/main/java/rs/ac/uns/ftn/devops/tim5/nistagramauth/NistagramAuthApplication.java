package rs.ac.uns.ftn.devops.tim5.nistagramauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NistagramAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(NistagramAuthApplication.class, args);
    }

}
