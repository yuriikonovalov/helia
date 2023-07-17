package com.yuriikonovalov.helia.data.api.service

import com.yuriikonovalov.helia.data.api.model.HotelProfile
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random


private val reviewAuthors = listOf(
    HotelProfile.Review.Author(
        name = "Emily Johnson",
        avatarUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=461&q=80"
    ),
    HotelProfile.Review.Author(
        name = "Robert Harris",
        avatarUrl = "https://images.unsplash.com/photo-1543610892-0b1f7e6d8ac1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80"
    ),
    HotelProfile.Review.Author(
        name = "Alex Turner",
        avatarUrl = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80"
    ),
    HotelProfile.Review.Author(
        name = "Linda Parker",
        avatarUrl = "https://plus.unsplash.com/premium_photo-1677368597077-009727e906db?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80"
    ),
    HotelProfile.Review.Author(
        name = "Olivia Turner",
        avatarUrl = "https://images.unsplash.com/photo-1558898479-33c0057a5d12?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80"
    )
)

private val reviews = listOf(
    5 to "A truly exceptional stay. The staff went above and beyond to make our visit memorable. The amenities were top-notch.",
    4 to "Decent hotel with friendly staff. The restaurant could have more diverse options on the menu.",
    4 to "Lovely views and a relaxing ambiance. The swimming pool was a highlight.",
    5 to "Trendy and comfortable. The hotel facilities are excellent, especially the fitness center.",
    5 to "Great value for the location. The meeting room was well-equipped for my business needs.",
    5 to "Incredible ambiance and an attentive staff. The meeting room facilities were top-notch for our corporate event.",
    4 to "An exquisite stay with every detail meticulously taken care of. The restaurant's culinary offerings were a delight",
    3 to "Overpriced for the experience. The restaurant staff seemed disorganized, and the Wi-Fi barely worked during my stay.",
    2 to "Expected more from a luxury hotel. The fitness center lacked proper maintenance, and the room service was slow.",
    3 to "Disappointing stay overall. The view was the only highlight; the rest of the amenities felt outdated.",
    3 to "Misleading photos online. The hotel felt outdated, and the restaurant's food quality did not match the prices.",
    5 to "Central location and attentive staff. The hotel's modern design and facilities exceeded my expectations",
    5 to "Ideal for a short stay in the city. The convenience of having a well-equipped fitness center was a definite plus.",
    4 to "Stylish and compact, perfect for solo travelers. The 24/7 Wi-Fi access was a lifesaver for my work commitments.",
    5 to "A hidden gem by the riverbank. The hotel's serene atmosphere made my vacation truly memorable."
)


private fun randomDate(): LocalDate {
    val year = Random.nextInt(from = 2021, until = 2023)
    val month = Random.nextInt(from = 1, until = 7)
    val day = Random.nextInt(from = 1, until = 28)
    return LocalDate.of(year, month, day)
}

private fun randomReviews(): List<HotelProfile.Review> {
    val count = Random.nextInt(from = 1, until = 15)
    val result = mutableListOf<HotelProfile.Review>()
    repeat(count) {
        val review = reviews.random()
        result.add(
            HotelProfile.Review(
                author = reviewAuthors.random(),
                rating = review.first,
                text = review.second,
                created = randomDate()
            )
        )
    }
    return result
}


private val hotelNames = listOf(
    "Luxury Haven Hotel",
    "Riverside Retreat",
    "Serenity Sands Resort",
    "Crystal Haven Inn",
    "Whispering Pines Lodge",
    "Moonlit Mirage Hotel",
    "Enchanted Oasis Retreat",
    "Velvet Horizon Suites",
    "Radiant Meadows Inn",
    "Azure Breeze Lodge",
    "Twilight Grove Manor",
    "Golden Crest Resort",
    "Starlight Haven Hotel",
    "Tranquil Harbor Retreat",
    "Misty Peaks Lodge",
    "Aurora Skies Inn",
    "Secret Garden Manor",
    "Harmony Springs Retreat",
    "Dreamscape Suites",
    "Celestial Shores Resort",
    "Emberwood Lodge",
    "Midnight Velvet Inn"
)

private val hotelAddresses = listOf(
    HotelProfile.Location(
        longitude = "-0.076132",
        latitude = "51.508530"
    ) to HotelProfile.Address(
        country = "England",
        city = "London",
        addressLine1 = "123 Park Lane"
    ),
    HotelProfile.Location(
        longitude = "-0.076132",
        latitude = "51.508530"
    ) to HotelProfile.Address(
        country = "England",
        city = "London",
        addressLine1 = "123 Park Lane"
    ),
    HotelProfile.Location(
        longitude = "-0.076132",
        latitude = "51.508530"
    ) to HotelProfile.Address(
        country = "England",
        city = "London",
        addressLine1 = "78 High Street"
    ),
    HotelProfile.Location(
        longitude = "-2.244644",
        latitude = "53.483959"
    ) to HotelProfile.Address(
        country = "England",
        city = "Manchester",
        addressLine1 = "17 Willow Crescent"
    ),
    HotelProfile.Location(
        longitude = "-1.898575",
        latitude = "52.489471"
    ) to HotelProfile.Address(
        country = "England",
        city = "Birmingham",
        addressLine1 = "8 Ivy Lane"
    ),
    HotelProfile.Location(
        longitude = "2.154007",
        latitude = "41.390205"
    ) to HotelProfile.Address(
        country = "Spain",
        city = "Barcelona",
        addressLine1 = "Calle del Sol, 23"
    ),
    HotelProfile.Location(
        longitude = "2.154007",
        latitude = "41.390205"
    ) to HotelProfile.Address(
        country = "Spain",
        city = "Madrid",
        addressLine1 = "Plaza Rosal, 8"
    ),
    HotelProfile.Location(
        longitude = "-3.703790",
        latitude = "40.416775"
    ) to HotelProfile.Address(
        country = "Spain",
        city = "Madrid",
        addressLine1 = "Avenida del Mar, 42"
    ),
    HotelProfile.Location(
        longitude = "-5.994072",
        latitude = "37.392529"
    ) to HotelProfile.Address(
        country = "Spain",
        city = "Seville",
        addressLine1 = "Carrer de la Luna, 15"
    ),
    HotelProfile.Location(
        longitude = "-89.650002",
        latitude = "39.799999"
    ) to HotelProfile.Address(
        country = "USA",
        city = "Springfield",
        addressLine1 = "456 Oak Avenue"
    ),
    HotelProfile.Location(
        longitude = "-122.083855",
        latitude = "37.386051"
    ) to HotelProfile.Address(
        country = "USA",
        city = "Mountainview",
        addressLine1 = "555 Birch Court"
    ),
    HotelProfile.Location(
        longitude = "-122.335167",
        latitude = "47.608013"
    ) to HotelProfile.Address(
        country = "USA",
        city = "Seattle",
        addressLine1 = "567 Meadow Lane"
    ),
    HotelProfile.Location(
        longitude = "-80.191788",
        latitude = "25.761681"
    ) to HotelProfile.Address(
        country = "USA",
        city = "Miami",
        addressLine1 = "890 Sunset Boulevard"
    ),
    HotelProfile.Location(
        longitude = "-122.676483",
        latitude = "45.523064"
    ) to HotelProfile.Address(
        country = "USA",
        city = "Portland",
        addressLine1 = "678 Lakeview Drive"
    ),
    HotelProfile.Location(
        longitude = "12.496366",
        latitude = "41.902782"
    ) to HotelProfile.Address(
        country = "Italy",
        city = "Roma",
        addressLine1 = "Via Roma, 10"
    ),
    HotelProfile.Location(
        longitude = "9.188540",
        latitude = "45.464664"
    ) to HotelProfile.Address(
        country = "Italy",
        city = "Milano",
        addressLine1 = "Corso Milano, 5"
    ),
    HotelProfile.Location(
        longitude = "12.327145",
        latitude = "45.438759"
    ) to HotelProfile.Address(
        country = "Italy",
        city = "Venezia",
        addressLine1 = "Via Venezia, 15"
    ),
    HotelProfile.Location(
        longitude = "14.305573",
        latitude = "40.853294"
    ) to HotelProfile.Address(
        country = "Italy",
        city = "Napoli",
        addressLine1 = "Via Napoli, 20"
    )
)

val countries = hotelAddresses.map { it.second.country }.toSet().toList()

private fun randomHotelInformation(): HotelProfile.HotelInformation {
    val numberOfBedrooms = Random.nextInt(from = 1, until = 5)
    val numberOfBathrooms = Random.nextInt(from = 1, until = numberOfBedrooms + 1)
    val squareMeters = Random.nextInt(from = 100, until = 350)
    return HotelProfile.HotelInformation(
        numberOfBedrooms = numberOfBedrooms,
        numberOfBathrooms = numberOfBathrooms,
        squareMeters = squareMeters
    )
}

private val photoUrls = listOf(
    "https://images.unsplash.com/photo-1455587734955-081b22074882?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1496417263034-38ec4f0b665a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1171&q=80",
    "https://images.unsplash.com/photo-1679678691263-cc7f30ce02f8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=388&q=80",
    "https://images.unsplash.com/photo-1618773928121-c32242e63f39?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1332&q=80",
    "https://images.unsplash.com/photo-1522798514-97ceb8c4f1c8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=435&q=80",
    "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1174&q=80",
    "https://images.unsplash.com/photo-1611892440504-42a792e24d32?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1568084680786-a84f91d1153c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=774&q=80",
    "https://plus.unsplash.com/premium_photo-1678286769762-b6291545d818?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1518733057094-95b53143d2a7?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=365&q=80",
    "https://images.unsplash.com/photo-1596701062351-8c2c14d1fdd0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80",
    "https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://plus.unsplash.com/premium_photo-1678297269980-16f4be3a15a6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1521783988139-89397d761dce?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=725&q=80",
    "https://images.unsplash.com/photo-1596394516093-501ba68a0ba6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1554647286-f365d7defc2d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80",
    "https://images.unsplash.com/photo-1551632436-cbf8dd35adfa?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1171&q=80",
    "https://images.unsplash.com/photo-1596436889106-be35e843f974?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1561384932-145921ce5214?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=435&q=80",
    "https://images.unsplash.com/photo-1554435517-12c44b0be193?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=435&q=80",
    "https://images.unsplash.com/photo-1600617547577-a486ad1c123d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2067&q=80",
    "https://images.unsplash.com/photo-1585436626103-d0035f8cf261?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80",
    "https://plus.unsplash.com/premium_photo-1664299647097-7edd0d770ec7?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1474690455603-a369ec1293f9?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
    "https://images.unsplash.com/photo-1455382054916-9c12746cfb43?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=465&q=80",
    "https://plus.unsplash.com/premium_photo-1681922761648-d5e2c3972982?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
)

private fun randomPhotoUrls(): List<String> {
    val count = Random.nextInt(from = 5, until = 15)
    val result = mutableSetOf<String>()
    repeat(count) {
        result.add(photoUrls.random())
    }
    return result.toList()
}

private fun randomHotelFacilities(): List<HotelFacility> {
    val hotelFacilities = HotelFacility.values()
    val count = Random.nextInt(from = 4, until = hotelFacilities.size + 1)
    return hotelFacilities.take(count)
}

private fun randomPrice() = Random.nextInt(from = 35, until = 500)

private val hotelDescriptions = listOf(
    "Experience unparalleled luxury and comfort at the Luxury Haven Hotel. Located in the heart of London, this hotel offers breathtaking views of the city skyline and impeccable service.",
    "Enjoy a peaceful escape at the Riverside Retreat. Nestled along the Thames River, this hotel offers a serene atmosphere and easy access to London's attractions",
    "Experience modern sophistication at the Urban Elegance Suites. Located in the bustling city center, this hotel offers chic accommodations and easy access to shopping and entertainment.",
    "Escape to a coastal haven where the rhythm of the waves soothes your soul. Luxurious rooms and suites offer stunning ocean views, while the sound of seagulls creates a tranquil ambiance. Explore pristine beaches, indulge in fresh seafood, and let the sea breeze carry your worries away.",
    "Immerse yourself in the heartbeat of the city from the comfort of our modern urban oasis. Stylish rooms provide a sanctuary amid the urban hustle, and panoramic cityscapes light up your nights. Savor diverse culinary offerings, discover cultural landmarks, and experience the vibrant energy of city life.",
    "Retreat to a mountain lodge where nature's majesty surrounds you. Cozy cabins and chalets offer warmth and comfort, while towering peaks create a breathtaking backdrop. Explore hiking trails, breathe in the crisp mountain air, and find solace in the embrace of the great outdoors.",
    "Step back in time at our historic establishment where stories of the past come alive. Elegant rooms are adorned with period furnishings, and the architecture reflects a bygone era. Wander through charming cobblestone streets, discover local traditions, and relish the timeless beauty of history.",
    "Find peace in the tranquil embrace of the countryside. Rustic lodges and cottages provide a cozy retreat, surrounded by rolling fields and picturesque landscapes. Immerse yourself in outdoor activities, enjoy farm-to-table cuisine, and let the unhurried pace rejuvenate your spirit.",
    "Experience contemporary comfort in the heart of modern convenience. Chic rooms offer sleek design and state-of-the-art amenities, while nearby attractions provide endless entertainment. Unwind by the pool, indulge in gourmet dining, and revel in the ease of a seamless urban escape.",
    "Discover an enchanting haven nestled within lush gardens. Quaint rooms and suites offer garden views, where vibrant blooms and serene pathways invite exploration. Immerse yourself in the beauty of nature, enjoy al fresco dining, and let the harmony of the gardens restore your balance.",
    "Seek adventure in the heart of the wilderness, where rugged charm meets untamed beauty. Cabins and lodges provide a rustic retreat, surrounded by towering trees and untrodden trails. Embark on outdoor expeditions, gather around campfires, and reconnect with the primal allure of the wild.",
    "Find seclusion in a timeless hideaway where tranquility reigns supreme. Intimate cottages and lodgings offer privacy and comfort, creating the perfect escape from the demands of daily life. Immerse yourself in meditative practices, embrace holistic wellness, and rediscover your inner harmony.",
    "Immerse yourself in the local culture from the heart of our vibrant establishment. Rooms reflect the region's traditions, while nearby markets, galleries, and performances offer a glimpse into the community's soul. Engage with local artisans, savor authentic cuisine, and let the cultural tapestry unfold around you."
)

fun randomHotels(count: Int): List<HotelProfile> {
    val result = mutableListOf<HotelProfile>()
    repeat(count) {
        val address = hotelAddresses.random()
        result.add(
            HotelProfile(
                id = UUID.randomUUID().toString(),
                name = hotelNames.random(),
                address = address.second,
                description = hotelDescriptions.random(),
                category = HotelCategory.values().random(),
                facilities = randomHotelFacilities(),
                price = randomPrice().toDouble(),
                imageUrls = randomPhotoUrls(),
                hotelInformation = randomHotelInformation(),
                location = address.first,
                reviews = randomReviews()
            )
        )
    }
    return result
}