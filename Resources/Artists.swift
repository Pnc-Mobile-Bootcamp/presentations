class Artist {
    let id: Int
    let name: String
    let genre: String
    let location: String
    let imageUrl: String
    let description: String
    let tags: String
    let login: String
    let password: String

    init(
        id: Int,
        name: String,
        genre: String,
        location: String,
        imageUrl: String,
        description: String,
        tags: String,
        login: String,
        password: String
    ) {
        self.id = id
        self.name = name
        self.genre = genre
        self.location = location
        self.imageUrl = imageUrl
        self.description = description
        self.tags = tags
        self.login = login
        self.password = password
    }
}

let artists: [Artist] = [
    Artist(
        id: 100,
        name: "High Voltage",
        genre: "Rock",
        location: "Los Angeles, CA",
        imageUrl: "/images/highvoltage.jpg",
        description: "This all-female classic rock/heavy metal band will get you up and moving.",
        tags: "Heavy Rock,Party,Loud",
        login: "voltage",
        password: "password"
    ),
    Artist(
        id: 101,
        name: "Selfie and the SimChips",
        genre: "Pop",
        location: "Miami, FL",
        imageUrl: "/images/selfiesim.jpg",
        description: "A current pop group fronted by a dynamic female singer.",
        tags: "Pop Music,Modern,Dance",
        login: "selfie",
        password: "password"
    ),
    Artist(
        id: 102,
        name: "Tony and Donna",
        genre: "Easy Listening",
        location: "New York, NY",
        imageUrl: "/images/donna.jpg",
        description: "A piano duo that has been entertaining audiences for over 12 years.",
        tags: "Piano,Duo,Adult Contemporary",
        login: "donna",
        password: "password"
    ),
    Artist(
        id: 105,
        name: "Carlos Dream",
        genre: "Rock",
        location: "New York, NY",
        imageUrl: "/images/CarlosDream.jpg",
        description: "Described by Entertainment Weekly as 'Barry White meets Al Green', Carlos will enchant you with his romantic, soulful sound.",
        tags: "Rock,Soul,Romance",
        login: "dream",
        password: "password"
    ),
    Artist(
        id: 104,
        name: "Joan Chandler",
        genre: "Mature",
        location: "New York, NY",
        imageUrl: "/images/chandler.jpg",
        description: "Joan's unique cultural and political viewpoint will have you laughing in your seat.",
        tags: "Comedian,Political,Mature",
        login: "chandler",
        password: "password"
    ),
    Artist(
        id: 103,
        name: "The Magnificent Marco",
        genre: "Family",
        location: "Chicago, IL",
        imageUrl: "/images/marco.jpg",
        description: "Family-friendly stage and street magic performed with a witty flair.",
        tags: "Street Magic,Juggling,Unicycle",
        login: "marco",
        password: "password"
    )
]