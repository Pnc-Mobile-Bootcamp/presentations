//
//  EnvironmentExtensions.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/11/26.
//

import SwiftUI

extension EnvironmentValues {
    
    var artistRepository: any RepositoryProtocol<Artist> {
        get { self[ArtistRepositoryKey.self] }
        set { self[ArtistRepositoryKey.self] = newValue }
    }
    
    
    var boardMemberRepository: any RepositoryProtocol<BoardMember> {
        get { self[BoardMemberRepositoryKey.self] }
        set { self[BoardMemberRepositoryKey.self] = newValue }
    }
    
    
    
}
