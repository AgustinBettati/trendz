package facultad.trendz;

import facultad.trendz.exception.post.PostNotFoundException;
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
        userRepository.save(new User("4@gmail.com", "Gon", passwordEncoder.encode("4"), role));
        userRepository.save(new User("mike@gmail.com", "Mike", passwordEncoder.encode("password"), role));
        userRepository.save(new User("olivia@gmail.com", "Olivia", passwordEncoder.encode("password"), role));
        userRepository.save(new User("john@gmail.com", "John", passwordEncoder.encode("password"), role));
        userRepository.save(new User("emma@gmail.com", "Emma", passwordEncoder.encode("password"), role));

        Long coloursId = topicRepository.save(new Topic("Colours", "Just colours", new Date())).getId();
        Long sportsId = topicRepository.save(new Topic("Sports", "", new Date())).getId();
        Long techNewsId = topicRepository.save(new Topic("Tech news", "The latest tech news", new Date())).getId();
        Long carsId = topicRepository.save(new Topic("Cars", "Posts about the best cars of the 21st Century", new Date())).getId();
        Long gamesId = topicRepository.save(new Topic("Video Games", "Discussions on the best video games", new Date())).getId();
        Long animalsId = topicRepository.save(new Topic("Animals", "Post about your favourite animals here", new Date())).getId();
        Long countriesId = topicRepository.save(new Topic("Countries", "Best countries to visit on your next holiday", new Date())).getId();
        Long moviesId = topicRepository.save(new Topic("Movies","Here we talk about recent theatrical releases", new Date())).getId();
        Long recipesId = topicRepository.save(new Topic("Recipes","Here we share our favourite recipes :)", new Date())).getId();
        Long booksId = topicRepository.save(new Topic("Books","Post about your new favourite book here!", new Date())).getId();

        //movies
        addPost("Star Wars", "Han Solo was the best","https://www.starwars.com/", moviesId, "1@gmail.com");
        addPost("Star Trek","Loved This Movie","https://intl.startrek.com/", moviesId, "3@gmail.com");
        addPost("Twilight","My daughter loved the werewolfs","https://en.wikipedia.org/wiki/Twilight_(2008_film)", moviesId, "2@gmail.com");
        addPost("Casablanca","The plane scene made me cry","https://en.wikipedia.org/wiki/Casablanca_(film)", moviesId, "1@gmail.com");

        Long movie5Id = addPost("The Godfather","Pretty much flawless, and one of the greatest films ever made","https://en.wikipedia.org/wiki/The_Godfather", moviesId, "1@gmail.com");
        addComment("The Pioneer of All Filmmaking", "mike@gmail.com", movie5Id);
        addVote("mike@gmail.com", movie5Id, true);
        addComment("Legendary Movie", "emma@gmail.com", movie5Id);
        addVote("emma@gmail.com", movie5Id, true);
        addComment("Never Fails", "olivia@gmail.com", movie5Id);
        addVote("olivia@gmail.com", movie5Id, true);

        Long movie6Id = addPost("The Dark Knight","The Batman of our dreams! So much more than a comic book movie","https://www.warnerbros.com/movies/dark-knight", moviesId, "4@gmail.com");
        addComment("Fantastic!", "john@gmail.com", movie6Id);
        addVote("john@gmail.com", movie6Id, true);
        addComment("Sometimes villains steal the show", "mike@gmail.com", movie6Id);
        addVote("mike@gmail.com", movie6Id, true);
        addComment("Best. Comic. Movie. EVER.", "emma@gmail.com", movie6Id);
        addVote("emma@gmail.com", movie6Id, true);

        Long movie7Id = addPost("Interstellar","An Emotional, Beautiful Journey into the Unknown","https://en.wikipedia.org/wiki/Interstellar_(film)", moviesId, "1@gmail.com");
        addComment("An Iconic Film", "mike@gmail.com", movie7Id);
        addVote("mike@gmail.com", movie7Id, true);
        addComment("The greatest movie of all time!", "emma@gmail.com", movie7Id);
        addVote("emma@gmail.com", movie7Id, true);
        addComment("Massively overrated.", "olivia@gmail.com", movie7Id);
        addVote("olivia@gmail.com", movie7Id, false);

        Long movie8Id = addPost("Parasite","You name a genre, this movie covers it","https://en.wikipedia.org/wiki/Parasite_(2019_film)", moviesId, "4@gmail.com");
        addComment("A true masterpiece.", "john@gmail.com", movie8Id);
        addVote("john@gmail.com", movie8Id, true);
        addComment("Perhaps I just did not get it", "mike@gmail.com", movie8Id);
        addVote("mike@gmail.com", movie8Id, false);
        addComment("One of the best films of this decade", "emma@gmail.com", movie8Id);
        addVote("emma@gmail.com", movie8Id, true);

        //recipes
        addPost("Steamed Potatoes","For this recipe you'll need potatoes, salt and pepper.","alink", recipesId, "2@gmail.com");
        addPost("Ham and Spagetti Sandwich","Unusual, but actually very tasty","https://es.wikipedia.org/wiki/Star_Trek", recipesId, "3@gmail.com");
        addPost("Bananna Cupacake","Ingredients: Bananna, Flour, Milk, Eggs and Sugar","alink", recipesId, "1@gmail.com");
        addPost("Chocolate-chip Brownies","You'll need chocolate, flour,milk and lots of love!","https://es.wikipedia.org/wiki/Star_Trek", recipesId, "1@gmail.com");

        Long recipe1Id = addPost("Slow Cooker Beef Stew","A hearty, savory slow cooker stew with potatoes, carrots, celery, broth, herbs and spices. You won't be slow to say 'yum'!","", recipesId, "2@gmail.com");
        addComment("Not good", "john@gmail.com", recipe1Id);
        addVote("john@gmail.com", recipe1Id, false);
        addComment("Much better than the recipe I've been using all these years!", "olivia@gmail.com", recipe1Id);
        addVote("olivia@gmail.com", recipe1Id, true);
        addComment("Never Fails", "emma@gmail.com", recipe1Id);
        addVote("emma@gmail.com", recipe1Id, true);

        Long recipe2Id =addPost("Garlic Salmon","A large salmon filet, steamed in foil and cooked either in the oven or barbecue. It's seasoned with minced garlic, fresh baby dill, lemon slices, fresh ground pepper and green onions.","https://www.allrecipes.com/recipe/20193/garlic-salmon/", recipesId, "3@gmail.com");
        addComment("Best salmon recipe I've ever tried", "john@gmail.com", recipe2Id);
        addVote("john@gmail.com", recipe2Id, true);
        addComment("So easy and tasty!", "mike@gmail.com", recipe2Id);
        addVote("mike@gmail.com", recipe2Id, true);
        addComment("Never Fails, this is my go to salmon recipe", "olivia@gmail.com", recipe2Id);
        addVote("olivia@gmail.com", recipe2Id, true);

        Long recipe3Id = addPost("Blueberry Muffins","These muffins are extra large and yummy with the sugary-cinnamon crumb topping","", recipesId, "1@gmail.com");
        addComment("These are delicious!", "mike@gmail.com", recipe3Id);
        addVote("mike@gmail.com", recipe3Id, true);
        addComment("These muffins are terrible!", "emma@gmail.com", recipe3Id);
        addVote("emma@gmail.com", recipe3Id, false);
        addComment("These should be called Blandberry Muffins", "olivia@gmail.com", recipe3Id);
        addVote("olivia@gmail.com", recipe3Id, false);

        Long recipe4Id = addPost("Homemade Mac and Cheese","Simple mac-n-cheese.","", recipesId, "1@gmail.com");
        addComment("Very Good!", "mike@gmail.com", recipe4Id);
        addVote("mike@gmail.com", recipe4Id, true);
        addComment("Excellent recipe!", "emma@gmail.com", recipe4Id);
        addVote("emma@gmail.com", recipe4Id, true);
        addComment("Never Fails", "olivia@gmail.com", recipe4Id);
        addVote("olivia@gmail.com", recipe4Id, true);

        //books
        addPost("1984, by George Orwell","Among the seminal texts of the 20th century, Nineteen Eighty-Four is a rare work that grows more haunting as its futuristic purgatory becomes more real. Published in 1949, the book offers political satirist George Orwell's nightmarish vision of a totalitarian, bureaucratic world and one poor stiff's attempt to find individuality. The brilliance of the novel is Orwell's prescience of modern life—the ubiquity of television, the distortion of the language—and his ability to construct such a thorough version of hell. Required reading for students since it was published, it ranks among the most terrifying novels ever written.","", booksId, "3@gmail.com");
        addPost("The Great Gatsby, by Francis Scott Fitzgerald","One of the great classics of twentieth-century literature.","https://en.wikipedia.org/wiki/The_Great_Gatsby", booksId, "1@gmail.com");

        Long book4Id = addPost("The Strange Case of Dr Jekyll and Mr Hyde","I just love curling up with a hot cup of tea and this book in my hand!","https://es.wikipedia.org/wiki/Star_Trek", booksId, "3@gmail.com");
        addComment("Such a great classic!", "mike@gmail.com", book4Id);
        addVote("mike@gmail.com", book4Id, true);

        Long book1Id = addPost("The Martian Chronicles, by Ray Braudbury","I love this books intriging take on the future","", booksId, "1@gmail.com");
        addComment("Ray Bradbury's writing is literally flawless.", "mike@gmail.com", book1Id);
        addVote("mike@gmail.com", book1Id, true);
        addComment("How wonderful, strange and poetic this book was.", "emma@gmail.com", book1Id);
        addVote("emma@gmail.com", book1Id, true);
        addComment("\"We earth men have a talent for ruining big, beautiful things.\"", "olivia@gmail.com", book1Id);

        Long book2Id = addPost("To Kill a Mockingbird, by Harper Lee","The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it, To Kill A Mockingbird became both an instant bestseller and a critical success when it was first published in 1960","https://en.wikipedia.org/wiki/To_Kill_a_Mockingbird", booksId, "1@gmail.com");
        addComment("Beautiful book.", "mike@gmail.com", book2Id);
        addVote("mike@gmail.com", book2Id, true);
        addComment("This is one of the most over-rated books in history", "emma@gmail.com", book2Id);

        Long book3Id = addPost("Foundation, by Issac Asimov","A bit confusing but i read it till the end!","alink", booksId, "2@gmail.com");
        addComment("Honestly, I don't get why this book/series is so popular", "mike@gmail.com", book3Id);
        addVote("mike@gmail.com", book3Id, false);
        addComment("Absolutely Loved it! Hail Asimov! He is brilliant! His writing is enchanting", "emma@gmail.com", book3Id);
        addVote("emma@gmail.com", book3Id, true);
        addComment("Great book!", "olivia@gmail.com", book3Id);
        addVote("olivia@gmail.com", book3Id, true);

        //cars
        Long car1Id = addPost("McLaren F1", "The McLaren F1 is a sports car designed and manufactured by British automobile manufacturer McLaren Cars, and powered by the BMW S70/2 V12 engine", "", carsId, "1@gmail.com");
        addVote("1@gmail.com", car1Id, true);
        addVote("3@gmail.com", car1Id, true);
        addVote("4@gmail.com", car1Id, true);
        addComment("My dream car!", "mike@gmail.com", car1Id);

        Long car2Id = addPost("Ferrari Enzo", "The Enzo Ferrari, or Type F140, is a 12-cylinder mid-engine sports car named after the company's founder, Enzo Ferrari. It was developed in 2002 using Formula One technology, such as a carbon-fibre body, F1-style automated-shift manual transmission, and carbon fibre-reinforced silicon carbide (C/SiC) ceramic composite disc brakes", "", carsId, "2@gmail.com");
        addVote("1@gmail.com", car2Id, true);
        addVote("2@gmail.com", car2Id, false);
        addVote("3@gmail.com", car2Id, false);
        addVote("4@gmail.com", car2Id, true);
        addComment("Great Post!", "john@gmail.com", car2Id);


        Long car3Id = addPost("Porsche Carrera GT", "The Porsche Carrera GT (Project Code 980) is a mid-engine sports car that was manufactured by German automobile manufacturer Porsche between 2004–2007. Sports Car International named the Carrera GT number one on its list of Top Sports Cars of the 2000s, and number eight on Top Sports Cars of All Time list", "", carsId, "3@gmail.com");
        addVote("1@gmail.com", car3Id, true);
        addVote("4@gmail.com", car3Id, true);

        Long car4Id = addPost("Mercedes-Benz SLR McLaren", "The Mercedes-Benz SLR McLaren (C199 / R199 / Z199) is a grand tourer jointly developed by German automotive manufacturer Mercedes-Benz and British automobile manufacturer McLaren Automotive and sold from 2003 to 2010", "", carsId, "4@gmail.com");
        addVote("2@gmail.com", car4Id, false);

        Long car5Id = addPost("Porsche 918 Spyder", "The Porsche 918 Spyder is a limited-production mid-engine plug-in hybrid sports car manufactured by German automobile manufacturer Porsche. The 918 Spyder is powered by a naturally aspirated 4.6 L (4,593 cc) V8 engine, developing 447 kW (608 PS; 599 hp) at 8,700 rpm, with two electric motors.", "", carsId, "1@gmail.com");
        addVote("1@gmail.com", car5Id, true);
        addVote("2@gmail.com", car5Id, true);
        addVote("3@gmail.com", car5Id, true);
        addVote("4@gmail.com", car5Id, true);
        addComment("Honestly, I don't get why this book/series is so popular", "mike@gmail.com", car5Id);

        Long car6Id = addPost("Bugatti Chiron", "The Bugatti Chiron is a mid-engine two-seater sports car developed and manufactured in Molsheim, France by French automobile manufacturer Bugatti Automobiles S.A.S.. The successor to the Bugatti Veyron, the Chiron was first shown at the Geneva Motor Show on 1 March 2016", "", carsId, "2@gmail.com");
        addVote("1@gmail.com", car6Id, true);
        addVote("3@gmail.com", car6Id, true);
        addVote("4@gmail.com", car6Id, true);
        addComment("Great Post!", "john@gmail.com", car6Id);
        addComment("Coolest car ever!", "mike@gmail.com", car6Id);

        Long car7Id = addPost("Koenigsegg Regera", "The Koenigsegg Regera is a limited production, plug-in hybrid grand touring sports car manufactured by Swedish automotive manufacturer Koenigsegg. It was unveiled at the March 2015 Geneva Motor Show. The name Regera is a Swedish verb, meaning \"to reign\" or \"to rule.\"", "", carsId, "1@gmail.com");
        addVote("1@gmail.com", car7Id, true);
        addVote("2@gmail.com", car7Id, false);
        addVote("3@gmail.com", car7Id, true);
        addVote("4@gmail.com", car7Id, true);

        //animals
        Long animal1Id = addPost("African lion", "The lion is a species in the family Felidae and a member of the genus Panthera. It has a muscular, deep-chested body, short, rounded head, round ears, and a hairy tuft at the end of its tail", "", animalsId, "1@gmail.com");
        addVote("1@gmail.com", animal1Id, true);
        addVote("2@gmail.com", animal1Id, false);
        addVote("4@gmail.com", animal1Id, true);

        Long animal2Id = addPost("Crocodile", "Crocodiles or true crocodiles are large semiaquatic reptiles that live throughout the tropics in Africa, Asia, the Americas and Australia. Crocodylinae, all of whose members are considered true crocodiles, is classified as a biological subfamily", "", animalsId, "2@gmail.com");
        addVote("2@gmail.com", animal2Id, false);
        addVote("3@gmail.com", animal2Id, true);

        Long animal3Id = addPost("Raccoon", "The raccoon is a medium-sized mammal native to North America. It is the largest of the procyonid family, having a body length of 40 to 70 cm (16 to 28 in) and a body weight of 5 to 26 kg (11 to 57 lb)", "", animalsId, "3@gmail.com");
        addVote("1@gmail.com", animal3Id, true);
        addVote("3@gmail.com", animal3Id, true);
        addVote("4@gmail.com", animal3Id, true);

        Long animal4Id = addPost("Civet", "A civet is a small, lean, mostly nocturnal mammal native to tropical Asia and Africa, especially the tropical forests. The term civet applies to over a dozen different mammal species. Most of the species diversity is found in southeast Asia", "", animalsId, "4@gmail.com");
        addVote("2@gmail.com", animal4Id, false);
        addVote("3@gmail.com", animal4Id, true);
        addVote("4@gmail.com", animal4Id, true);

        Long animal5Id = addPost("Quoll", "Quolls are carnivorous marsupials native to Australia and New Guinea. They are primarily nocturnal and spend most of the day in a den. Of the six species of quoll, four are found in Australia and two in New Guinea", "", animalsId, "1@gmail.com");
        addVote("2@gmail.com", animal5Id, false);

        Long animal6Id = addPost("White-throated toucan", "The white-throated toucan is a near-passerine bird in the family Ramphastidae found in South America throughout the Amazon Basin including the adjacent Tocantins and Araguaia River drainage. It prefers tropical humid forest, but also occurs in woodland and locally in riverine forest within cerrado. This bird can be found in Bolivia in the Beni Departement, notably in the city of Riberalta and in the close by Aquicuana Reserve.", "", animalsId, "2@gmail.com");
        addVote("4@gmail.com", animal6Id, true);

        //colours
        addPost("Crimson", "", "", coloursId, "1@gmail.com");
        addPost("Orange", "", "", coloursId, "2@gmail.com");
        addPost("Red", "", "", coloursId, "3@gmail.com");
        addPost("Yellow", "", "", coloursId, "4@gmail.com");
        addPost("Maroon", "", "", coloursId, "1@gmail.com");
        addPost("Blue", "", "", coloursId, "2@gmail.com");
        addPost("Purple", "", "", coloursId, "2@gmail.com");
        addPost("Indigo", "", "", coloursId, "4@gmail.com");

        //countries
        Long country1Id = addPost("Brazil", "From the jungle calls of the Amazon to the thong-clad crowds of Copacabana beach, Brazil is an intoxicating mix of the big, the bold and the beautiful, perennially one of the world’s favourite destinations.", "https://www.visitbrasil.com/", countriesId, "1@gmail.com");
        addVote("1@gmail.com", country1Id, true);
        addVote("2@gmail.com", country1Id, false);
        addVote("3@gmail.com", country1Id, true);
        addVote("4@gmail.com", country1Id, true);
        addComment("Amazing place!", "john@gmail.com", country1Id);


        Long country2Id = addPost("Nigeria", "From the loud laughter of traffic-choked Lagos to the green-fringed villages that line rivers and streams, Nigeria is one of the most exciting places to visit in Africa.", "https://en.wikipedia.org/wiki/Nigeria", countriesId, "2@gmail.com");
        addVote("1@gmail.com", country2Id, true);
        addVote("2@gmail.com", country2Id, true);
        addVote("3@gmail.com", country2Id, true);

        Long country3Id = addPost("Japan", "From kimono-clad geishas singing karaoke in Kyoto to Buddhist monks whizzing around Tokyo on motorbikes, Japan is a fascinating land of contrasts, a heady mix of tradition and modernity that often bewilders but never bores.", "https://www.japan.go.jp/", countriesId, "3@gmail.com");
        addVote("2@gmail.com", country3Id, false);
        addVote("3@gmail.com", country3Id, true);
        addVote("4@gmail.com", country3Id, false);
        addComment("Japan is my favourite country!", "emma@gmail.com", country3Id);

        Long country4Id = addPost("Cameroon", "While referring to Cameroon as “Africa in miniature” has become a bit of a cliché, this statement certainly rings true: everything you would expect from the African continent seems to be consolidated here, in this enticing and eclectic land.", "https://www.prc.cm/en/", countriesId, "4@gmail.com");
        addVote("1@gmail.com", country4Id, false);
        addVote("2@gmail.com", country4Id, true);

        Long country5Id = addPost("New Zealand", "Widely held to be one of the most breathtaking countries on the planet, New Zealand is a phenomenal travel destination. The rugged mountains and remote valleys that thrust the destination into the world’s spotlight through ", "https://www.govt.nz/", countriesId, "1@gmail.com");
        addVote("1@gmail.com", country5Id, true);
        addVote("2@gmail.com", country5Id, true);
        addVote("3@gmail.com", country5Id, true);
        addVote("4@gmail.com", country5Id, true);
        addComment("I need to visit this country!", "john@gmail.com", country5Id);

        Long country6Id = addPost("Poland", "Underrated but increasingly popular, Poland offers a huge amount for travellers of all stripes – from the stunning old towns of Krakow, Zamość, Gdańsk and Wroclaw to the wilderness of the Białowieża National Park with its ubiquitous buffalos and epic vistas.", "https://www.gov.pl/", countriesId, "2@gmail.com");
        addVote("1@gmail.com", country6Id, true);
        addVote("2@gmail.com", country6Id, true);
        addVote("3@gmail.com", country6Id, false);
        addComment("Definitely visiting poland on my next holidays", "john@gmail.com", country6Id);

        Long country7Id = addPost("France", "You could spend a lifetime's worth of holidays in France and still feel as if you hadn't done the country justice. It remains one the planet's most visited tourist destination, meriting its standing with an almost overwhelming mass of historical treasures, storybook landscapes, and cultural idiosyncrasies.", "https://en.wikipedia.org/wiki/France", countriesId, "3@gmail.com");
        addVote("1@gmail.com", country7Id, false);
        addVote("2@gmail.com", country7Id, false);
        addVote("3@gmail.com", country7Id, true);

        Long country8Id = addPost("Ukraine", "Vast and mysterious to many, Ukraine is barely known to outsiders despite being one of the largest countries in Europe. Long-associated with its colossal neighbour Russia, it's a country that stands out in its own right for its varied landscapes and surprising cultural diversity.", "https://mfa.gov.ua/", countriesId, "4@gmail.com");
        addVote("1@gmail.com", country8Id, true);

        Long country9Id = addPost("Guatemala", "Guatemala humbly has it all: from colonial towns to Mayan ruins, great mountain lakes to vibrant religious festivals, sandy beaches to exotic jungles. Often visitors to the country find they leave enlightened; civilisations they believed long gone are found thriving, Tomb Raider landscapes they thought fantasy are shown to be real.", "https://www.guatemala.gob.gt/", countriesId, "1@gmail.com");
        addVote("1@gmail.com", country9Id, true);
        addVote("3@gmail.com", country9Id, true);
        addVote("4@gmail.com", country9Id, false);

        //videogames
        Long game1Id = addPost("World of Warcraft", "Blizzard’s bracing 2004 fantasy simulation World of Warcraft introduced millions of players to the concept (and joys and frustrations) of massively multiplayer online worlds", "", gamesId, "1@gmail.com");
        addVote("1@gmail.com", game1Id, true);
        addVote("2@gmail.com", game1Id, false);
        addVote("3@gmail.com", game1Id, false);
        addVote("4@gmail.com", game1Id, true);
        addComment("So overrated!", "mike@gmail.com", game1Id);

        Long game2Id = addPost("The Legend of Zelda", "Sure, there's undeniable nostalgia associated with this 1986 NES classic, but there's no arguing how engrossing the original Zelda was to play. Mixing upgradable weapons with a (then) sprawling map and some pretty good puzzles, Link's original adventure delivered an experience unlike anything console players had experienced", "", gamesId, "2@gmail.com");
        addVote("1@gmail.com", game2Id, true);

        Long game3Id = addPost("Minecraft", "Swedish studio Mojang's indie bolt from the blue turns out to be that rare example of a game whose title perfectly sums up its gameplay: you mine stuff, then you craft it. At its simplest, Minecraft is a procedurally generated exercise in reorganizing bits of information—all those cubes of dirt and rock and ore strewn about landscapes plucked from 1980s computers—into recognizable objects and structures and mechanisms", "", gamesId, "3@gmail.com");
        addVote("1@gmail.com", game3Id, true);
        addVote("3@gmail.com", game3Id, true);
        addVote("4@gmail.com", game3Id, true);
        addComment("My kids love this game!", "mike@gmail.com", game3Id);

        Long game4Id = addPost("Doom", "Quick, name your favorite modern first-person shooter. Maybe it's Call of Duty, or Halo, or Counter-Strike. All of those games—and dozens, if not hundreds more—owe an immense debt to Doom", "", gamesId, "4@gmail.com");
        addVote("1@gmail.com", game4Id, true);
        addVote("2@gmail.com", game4Id, false);
        addVote("3@gmail.com", game4Id, false);
        addVote("4@gmail.com", game4Id, false);
        addComment("Hate this game!", "john@gmail.com", game4Id);

        Long game5Id = addPost("Super Mario 64", "Mario's brick-breaking, Goomba-stomping antics were enough to mesmerize the world's gamers in Nintendo's idiosyncratic side-scrolling Super Mario Bros. games. But 1996's Super Mario 64 transported Nintendo fans into Mario's universe as no other game in the series had, simultaneously laying out a grammar for how to interact with 3D worlds", "", gamesId, "1@gmail.com");
        addVote("1@gmail.com", game5Id, true);
        addVote("4@gmail.com", game5Id, true);
        addComment("Loved it!", "john@gmail.com", game5Id);

        Long game6Id = addPost("Tetris", "Designed by a Russian computer scientist, mass-distributed by a Japanese company and devoured by gamers—casual or compulsive—around the world, Tetris has been a global phenomenon since its arrival in 1984", "", gamesId, "2@gmail.com");
        addVote("1@gmail.com", game6Id, true);
        addVote("2@gmail.com", game6Id, false);
        addVote("4@gmail.com", game6Id, true);

        //tech news
        Long news1Id = addPost("Please comment your best Black Friday deals :)", "", "", techNewsId, "1@gmail.com");
        addVote("1@gmail.com", news1Id, true);

        Long news2Id = addPost("Where to buy PS5", "Finding PS5 restocks isn't easy. I'll point you to the right places. Amazon, Best Buy, Walmart, GameStop, Target, B&H Photo, Newegg", "", techNewsId, "2@gmail.com");
        addVote("1@gmail.com", news2Id, true);
        addVote("2@gmail.com", news2Id, true);
        addVote("4@gmail.com", news2Id, true);

        Long news3Id = addPost("Apple's new M1 chip", "Apple's all-new M1 chip has a space-saving, unified architecture, which brings a number of essential components closer together. Built using a 5-nanometer process, the M1 chip also has a large number of transistors (16 billion) packed into a tiny space. The cutting-edge architecture makes the chip more powerful and energy-efficient than its competitors, and will make your Mac the same. The M1 chip has a powerful 8-core CPU, designed to offer industry-leading performance and power efficiency. Half of the cores are made for demanding tasks, while the other half have a higher power efficiency that's ideal for lighter use.", "https://www.apple.com/macbook-air/", techNewsId, "3@gmail.com");
        addVote("1@gmail.com", news3Id, true);
        addVote("2@gmail.com", news3Id, false);
        addVote("4@gmail.com", news3Id, true);

        Long news4Id = addPost("Coronavirus: North Korea and Russia hackers 'targeting vaccine'", "State-backed hackers from North Korea and Russia have been targeting organisations working on a coronavirus vaccine, Microsoft has said.", "https://www.bbc.com/news/av/world-asia-china-54852131", techNewsId, "4@gmail.com");
        addVote("1@gmail.com", news4Id, false);
        addVote("2@gmail.com", news4Id, false);
        addVote("4@gmail.com", news4Id, false);

        Long news5Id = addPost("China sends 'world's first 6G' test satellite into orbit", "China has successfully launched what has been described as \"the world's first 6G satellite\" into space to test the technology. It went into orbit along with 12 other satellites from the Taiyuan Satellite Launch Center in the Shanxi Province.", "https://www.bbc.com/news/technology-54936886", techNewsId, "1@gmail.com");
        addVote("1@gmail.com", news5Id, true);
        addVote("2@gmail.com", news5Id, false);
        addVote("4@gmail.com", news5Id, true);

        //sports
        addPost("Lewis Hamilton wins Turkish GP to clinch record-equalling seventh F1 title", "", "https://www.theguardian.com/sport/2020/nov/15/lewis-hamilton-wins-turkish-gp-to-clinch-record-equalling-seventh-f1-world-title-michael-schumacher", sportsId, "1@gmail.com");
        addPost("Argentina beat New Zealand for first time with shock 25-15 victory.", "", "https://www.rnz.co.nz/news/sport/430635/rugby-argentina-make-history-with-25-15-defeat-of-all-blacks", sportsId, "2@gmail.com");
    }

    public Long addPost(String title, String description, String link, Long topicId, String userEmail){
        return postRepository.save(new Post(title, description, link, new Date(), topicRepository.getTopicById(topicId), userRepository.findByEmail(userEmail))).getId();
    }

    public void addComment(String content, String userEmail, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.getComments().add(new Comment(content, new Date(), userRepository.findByEmail(userEmail), post));
        postRepository.save(post);
    }

    public void addVote(String userEmail, Long postId, boolean upVote){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.getVotes().add(new Vote(post, userRepository.findByEmail(userEmail), upVote));
        postRepository.save(post);
    }
}
