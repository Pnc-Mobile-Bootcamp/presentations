//
//  SwiftUIDemoApp.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/8/26.
//

import SwiftUI

@main
struct SwiftUIDemoApp: App {
    
    let kazooAPIURL = "https://kazoopromotions.com/api"
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                // configure custom dependency injection
                .environment(\.artistRepository, RemoteArtistRepository(urlBase: kazooAPIURL))
                .environment(\.boardMemberRepository, MockBoardMemberRepository())
            
        }
    }
}
