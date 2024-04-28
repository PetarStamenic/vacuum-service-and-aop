package rs.raf.userbackend.mapper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rs.raf.userbackend.model.MyUser;
import rs.raf.userbackend.repository.UserRepository;

@Profile({"default"})
@Component
public class Seeder implements CommandLineRunner {
    private UserRepository userRepository;

    public Seeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        MyUser u1 = new MyUser();
        u1.setUsername("pera");
        u1.setPassword("pera");
        u1.setEmail("pera@pera.com");
        u1.setPermissions(0b1111111111);
        userRepository.save(u1);


        MyUser u2 = new MyUser();
        u2.setUsername("mika");
        u2.setPassword("mika");
        u2.setEmail("mika@mika.com");
        u2.setPermissions((byte)0b0000);
        userRepository.save(u2);


        MyUser u3 = new MyUser();
        u3.setUsername("djura");
        u3.setPassword("djura");
        u3.setEmail("djura@djura.com");
        u3.setPermissions((byte)0b0101);
        userRepository.save(u3);


        MyUser u4 = new MyUser();
        u4.setUsername("steva");
        u4.setPassword("steva");
        u4.setEmail("steva@steva.com");
        u4.setPermissions((byte)0b1010);
        userRepository.save(u4);
    }
}
