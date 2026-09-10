//
//  ArtistList.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//
import SwiftUI

struct ArtistList: View {
    
    @State private var viewModel = ViewModel()
    
    var body: some View {
        VStack {
            HStack {
                Text("Filter")
                TextField("Genre", text: $viewModel.filter)
            }
            .padding()
            List(viewModel.matchingArtists) { artist in
                HStack {
                    Text(artist.name)
                    Text(" - ")
                    Text(artist.genre)
                }
                .onTapGesture {
                    self.viewModel.selectedArtist = artist
                }
            }
            if let selected = viewModel.selectedArtist {
                Text(selected.name)
                    .font(Font.largeTitle)
                AsyncImage(url: URL(string:
                    "https://kazoopromotions.com/\(selected.imageUrl)")) { image in
                    image.resizable().scaledToFit()
                } placeholder: {
                    ProgressView()
                }
            }
            
        }
        .task {
            viewModel.loadArtists()
        }
    }
    
}



extension ArtistList {
    
    
    @Observable
    class ViewModel {
        
        var artists: [Artist] = [] {
            didSet {
                filter = ""
                selectedArtist = nil
            }
        }
        var filter: String = "" {
            didSet {
                matchingArtists = artists.filter { artist in
                    filter == "" ||
                    artist.genre.lowercased()
                        .contains(filter.lowercased())
                }
            }
        }
        var matchingArtists: [Artist] = [] {
            didSet {
                if let selected = selectedArtist,
                   !matchingArtists.contains(selected) {
                    selectedArtist = nil
                }
            }
        }
        var selectedArtist: Artist? = nil
        
        func loadArtists() {
            artists = [
                Artist(
                    id: 100,
                    name: "High Voltage",
                    genre: "Rock",
                    location: "Los Angeles, CA",
                    imageUrl: "/images/highvoltage.jpg",
                    description: "This all-female classic rock/heavy metal band will get you up and moving.",
                    tags: "Heavy Rock,Party,Loud"
                ),
                Artist(
                    id: 101,
                    name: "Selfie and the SimChips",
                    genre: "Pop",
                    location: "Miami, FL",
                    imageUrl: "/images/selfiesim.jpg",
                    description: "A current pop group fronted by a dynamic female singer.",
                    tags: "Pop Music,Modern,Dance"
                ),
                Artist(
                    id: 102,
                    name: "Tony and Donna",
                    genre: "Easy Listening",
                    location: "New York, NY",
                    imageUrl: "/images/donna.jpg",
                    description: "A piano duo that has been entertaining audiences for over 12 years.",
                    tags: "Piano,Duo,Adult Contemporary"
                ),
                Artist(
                    id: 105,
                    name: "Carlos Dream",
                    genre: "Rock",
                    location: "New York, NY",
                    imageUrl: "/images/CarlosDream.jpg",
                    description: "Described by Entertainment Weekly as 'Barry White meets Al Green', Carlos will enchant you with his romantic, soulful sound.",
                    tags: "Rock,Soul,Romance"
                ),
                Artist(
                    id: 104,
                    name: "Joan Chandler",
                    genre: "Mature",
                    location: "New York, NY",
                    imageUrl: "/images/chandler.jpg",
                    description: "Joan's unique cultural and political viewpoint will have you laughing in your seat.",
                    tags: "Comedian,Political,Mature"
                ),
                Artist(
                    id: 103,
                    name: "The Magnificent Marco",
                    genre: "Family",
                    location: "Chicago, IL",
                    imageUrl: "/images/marco.jpg",
                    description: "Family-friendly stage and street magic performed with a witty flair.",
                    tags: "Street Magic,Juggling,Unicycle"
                )
            ]        }
        
    }
    
}



#Preview {
    ArtistList()
}
