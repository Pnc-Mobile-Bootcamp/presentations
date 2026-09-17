//
//  ContentView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/8/26.
//

import SwiftUI

struct ContentView: View {
    @Environment(\.artistRepository) private var artistRepository
    @Environment(\.boardMemberRepository) private var boardRepository
    @EnvironmentObject var authStatus: AuthStatus
    
    @State private var current: String = ""
    
    
    var body: some View {
        NavigationStack {
            VStack {
                switch current {
                case "artists":
                    ArtistList(repository: artistRepository)
                case "board":
                    BoardMembersView(repository: boardRepository)
                case "employees":
                    EmployeeList()
                case "checkout":
                    CheckoutContainerView()
                case "cats":
                    UrlDemo()
                case "todo":
                    ToDoList()
                default:
                    Welcome()
                }
            }
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Menu {
                        Button("Artists") {
                            current = "artists"
                        }
                        Button("Board Members") {
                            current = "board"
                        }
                        Button("Employees") {
                            current = "employees"
                        }
                        Button("To Do List") {
                            current = "todo"
                        }
                        Divider()
                        Button("Checkout") {
                            current = "checkout"
                        }
                        Button("Cats") {
                            current = "cats"
                        }
                        Divider()
                        Button("Log out") {
                            authStatus.updateLoginStatus(success: false)
                        }
                    }
                    label: {
                        Label("View", systemImage: "line.3.horizontal")
                    }
                }
            }
        }
        
    }
}

#Preview {
    ContentView()
}
