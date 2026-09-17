//
//  Artist.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//

class Artist: Identifiable, Hashable, Codable {
    
    let id: Int
    let name: String
    let genre: String
    let location: String
    let imageUrl: String
    let description: String
    let tags: String

    init(
        id: Int, name: String, genre: String, location: String,
        imageUrl: String, description: String, tags: String,
    ) {
        self.id = id
        self.name = name
        self.genre = genre
        self.location = location
        self.imageUrl = imageUrl
        self.description = description
        self.tags = tags
    }

    static func == (lhs: Artist, rhs: Artist) -> Bool {
        lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }

}
