package facultad.trendz;

import facultad.trendz.model.*;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.RoleRepository;
import facultad.trendz.repository.TopicRepository;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
class OnStartLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TopicRepository topicRepository;
    private  final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public OnStartLoader(UserRepository userRepository, RoleRepository roleRepository, TopicRepository topicRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.topicRepository=topicRepository;
        this.postRepository=postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(ApplicationArguments args) {
        roleRepository.save(new Role(ERole.ROLE_ADMIN, 1L));
        roleRepository.save(new Role(ERole.ROLE_USER, 2L));

        Role role = roleRepository.getByEnumRole(ERole.ROLE_ADMIN);
        Role userRole = roleRepository.getByEnumRole(ERole.ROLE_USER);
        userRepository.save(new User("agustinbettati@gmail.com", "AgustinBettati", passwordEncoder.encode("password"), role));
        userRepository.save(new User("marcoskhabie@gmail.com", "MarcosKhabie", passwordEncoder.encode("password"), userRole));
        userRepository.save(new User("gonzalodeachaval@gmail.com", "GonzaloDeAchaval", passwordEncoder.encode("password"), role));
        userRepository.save(new User("florvimberg@gmail.com", "FlorenciaVimberg", passwordEncoder.encode("password"), role));
        userRepository.save(new User("admin@gmail.com", "admin", passwordEncoder.encode("admin"), role));
        userRepository.save(new User("user@gmail.com", "user", passwordEncoder.encode("user"), userRole));
        userRepository.save(new User("1@gmail.com", "Agustin", passwordEncoder.encode("1"), userRole));
        userRepository.save(new User("2@gmail.com", "Florencia", passwordEncoder.encode("2"), userRole));
        userRepository.save(new User("3@gmail.com", "Mark", passwordEncoder.encode("3"), role));


        topicRepository.save(new Topic("Movies","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Recipes","Here we share our favourite recipes :)", new Date()));
        topicRepository.save(new Topic("Books","Post about your new favourite book here!", new Date()));
        topicRepository.save(new Topic("Movies4","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies5","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies6","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies7","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies8","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies9","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies10","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies11","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies12","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies13","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies14","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies15","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies16","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies17","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies18","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies19","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies20","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies21","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies22","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1A2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1B2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1C2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1D2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1D2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1E2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1F2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1G2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies1H2","Here we talk about recent theatrical releases", new Date()));
        topicRepository.save(new Topic("Movies and Books","Un párrafo, también llamado parágrafo (del griego παράγραφος [parágraphos], y este de παρα, «próximo, semejante», y γραφος, «escritura»), es una unidad comunicativa formada por un conjunto de oraciones secuenciales que trata un mismo tema. Está compuesto por un conjunto de oraciones que tienen cierta unidad temática o que, sin tenerla, se enuncian juntas. Es un componente del texto que en su aspecto externo comienza con una mayúscula y termina en un punto y aparte. Comprende varias oraciones relacionadas sobre el mismo subtema; una de ellas expresa la idea principal.", new Date()));


        postRepository.save(new Post("Star Wars","Han Solo was the best","alink",new Date(),topicRepository.getTopicById(Long.valueOf(1)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Star Trek","Loved This Movie","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(1)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Twilight","My daughter loved the werewolfs","alink",new Date(),topicRepository.getTopicById(Long.valueOf(1)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Casablanca","The plane scene made me cry","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(1)),userRepository.findByEmail("1@gmail.com")));


        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("3@gmail.com")));
        postRepository.save(new Post("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(2)),userRepository.findByEmail("1@gmail.com")));

        postRepository.save(new Post("The Martian Chronicles, by Ray Braudbury","I love this books intriging take on the future","alink",new Date(),topicRepository.getTopicById(Long.valueOf(3)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Turning Point, BY Hayao Miyasaki","This book give great insight on the worlds greatest animator","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(3)),userRepository.findByEmail("1@gmail.com")));
        postRepository.save(new Post("Foundation, by Issac Asimov","A bit confusing but i read it till the end!","alink",new Date(),topicRepository.getTopicById(Long.valueOf(3)),userRepository.findByEmail("2@gmail.com")));
        postRepository.save(new Post("The Strange Case of Dr Jekyll and Mr Hyde","I just love curling up with a hot cup of tea and this book in my hand!","https://es.wikipedia.org/wiki/Star_Trek",new Date(),topicRepository.getTopicById(Long.valueOf(3)),userRepository.findByEmail("3@gmail.com")));
    }
}
