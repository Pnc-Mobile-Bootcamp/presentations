//
//  BoardMembersView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/11/26.
//
import SwiftUI

struct BoardMembersView: View {
    
    var repository: any RepositoryProtocol<BoardMember>
    @State var viewModel: ViewModel
    
    init(repository: any RepositoryProtocol<BoardMember>) {
        self.repository = repository
        viewModel = ViewModel(repository: repository)
    }
    
    var body: some View {
        VStack {
            BannerError(model: viewModel)
            List(viewModel.members) { member in
                HStack {
                    Text("\(member.firstName) \(member.lastName)")
                    Text(" - ")
                    Text(member.title)
                }
                .onTapGesture {
                    self.viewModel.selectedMember = member
                }
            }
            if let selected = viewModel.selectedMember {
                Text("\(selected.firstName) \(selected.lastName)")
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
            await viewModel.loadData()
        }
    }
    
}


extension BoardMembersView {
    
    @Observable
    class ViewModel: Failable {
        
        var repository: any RepositoryProtocol<BoardMember>
        var members: [BoardMember] = [] {
            didSet {
                selectedMember = nil
            }
        }
        var selectedMember: BoardMember? = nil
        var errorMessage: String = ""
        
        init(repository: any RepositoryProtocol<BoardMember>) {
            self.repository = repository
        }
        
        func loadData() async {
            errorMessage = ""
            do {
                members = try await repository.getAll()
                errorMessage = "Something went awry!"
            }
            catch {
                errorMessage = "\(error)"
            }
        }
        
        
    }
    
}


#Preview {
    BoardMembersView(repository: MockBoardMemberRepository())
}
