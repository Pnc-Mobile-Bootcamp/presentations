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
    
    var body: some View {
        // EmployeeList()
        // ArtistList(repository: artistRepository)
        // CheckoutContainerView()
        BoardMembersView(repository: boardRepository)
    }
}

#Preview {
    ContentView()
}
