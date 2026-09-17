//
//  RemoteArtistRepository.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/14/26.
//
import Foundation

class RemoteBoardMemberRepository: RemoteRepositoryBase<BoardMember>, RepositoryProtocol<BoardMember>  {
    
    private let urlBase: String
    
    init(urlBase: String, authStatus: AuthStatus) {
        self.urlBase = urlBase
        super.init(authStatus: authStatus)
    }
    
    func getAll() async throws -> [BoardMember] {
        let urlString = "\(urlBase)/boardmembers"
        return try await fetchAll(urlString)
    }
    
    func getById(_ id: Int) async throws -> BoardMember? {
        let urlString = "\(urlBase)/boardmembers/\(id)"
        return try await fetchOne(urlString)
    }
    
    func insert(_ item: BoardMember) async throws -> BoardMember {
        let urlString = "\(urlBase)/boardmembers"
        return try await post(urlString, send: item)
    }
    
    func update(_ item: BoardMember) async throws {
        let urlString = "\(urlBase)/boardmembers/\(item.id)"
        try await put(urlString, send: item)
    }
    
    func delete(_ item: BoardMember) async throws {
        let urlString = "\(urlBase)/boardmembers/\(item.id)"
        try await del(urlString)
    }
    
    
}
