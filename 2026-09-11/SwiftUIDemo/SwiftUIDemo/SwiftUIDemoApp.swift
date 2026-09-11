//
//  SwiftUIDemoApp.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/8/26.
//

import SwiftUI

@main
struct SwiftUIDemoApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                // configure custom dependency injection
                .environment(\.artistRepository, MockArtistRepository())
                .environment(\.boardMemberRepository, MockBoardMemberRepository())
            
        }
    }
}
