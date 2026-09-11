//
//  ArtistRepositoryKey.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/11/26.
//

// this is a key for the Environment object to store an Artist Repository for DI
import SwiftUI

struct ArtistRepositoryKey: EnvironmentKey {
    static let defaultValue: any RepositoryProtocol<Artist> = MockArtistRepository()
}

struct BoardMemberRepositoryKey: EnvironmentKey {
    static let defaultValue: any RepositoryProtocol<BoardMember> = MockBoardMemberRepository()
}


