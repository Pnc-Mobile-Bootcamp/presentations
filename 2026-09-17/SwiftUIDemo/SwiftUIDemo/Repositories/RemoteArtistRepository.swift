//
//  RemoteArtistRepository.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/14/26.
//
import Foundation

class RemoteArtistRepository: RemoteRepositoryBase<Artist>, RepositoryProtocol<Artist>  {
    
    private let urlBase: String
    
    init(urlBase: String, authStatus: AuthStatus) {
        self.urlBase = urlBase
        super.init(authStatus: authStatus)
    }
    
    func getAll() async throws -> [Artist] {
        let urlString = "\(urlBase)/talent"
        return try await fetchAll(urlString)
    }
    
    func getById(_ id: Int) async throws -> Artist? {
        let urlString = "\(urlBase)/talent/\(id)"
        return try await fetchOne(urlString)
    }
    
    func insert(_ item: Artist) async throws -> Artist {
        let urlString = "\(urlBase)/talent"
        return try await post(urlString, send: item)
    }
    
    func update(_ item: Artist) async throws {
        let urlString = "\(urlBase)/talent/\(item.id)"
        try await put(urlString, send: item)
    }
    
    func delete(_ item: Artist) async throws {
        let urlString = "\(urlBase)/talent/\(item.id)"
        try await del(urlString)
    }
    
    
}
