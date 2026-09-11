//
//  BoardMember.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/11/26.
//

class BoardMember: Identifiable, Hashable, Codable {
    let id: Int
    let firstName: String
    let lastName: String
    let title: String
    let gender: String
    let photo: String
    let imageUrl: String
    let bio: String

    init(id: Int, firstName: String, lastName: String, title: String, gender: String,
        photo: String, imageUrl: String, bio: String
    ) {
        self.id = id
        self.firstName = firstName
        self.lastName = lastName
        self.title = title
        self.gender = gender
        self.photo = photo
        self.imageUrl = imageUrl
        self.bio = bio
    }
    
    static func == (lhs: BoardMember, rhs: BoardMember) -> Bool {
        lhs.id == rhs.id
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
}
