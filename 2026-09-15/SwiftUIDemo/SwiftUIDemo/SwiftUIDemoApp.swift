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
    @StateObject var authStatus = AuthStatus()
    
    
    var body: some Scene {
        WindowGroup {
            if authStatus.isLoggedIn {
                ContentView()
                // configure custom dependency injection
                    .environment(\.artistRepository, RemoteArtistRepository(urlBase: kazooAPIURL,
                                                                           authStatus: authStatus))
                    .environment(\.boardMemberRepository, RemoteBoardMemberRepository(urlBase: kazooAPIURL,
                                                                                     authStatus: authStatus))
                    .environmentObject(authStatus)
            }
            else {
                LoginView()
                    .environmentObject(authStatus)
            }
        }
    }
}
