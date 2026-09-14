//
//  RepositoryProtocol.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/11/26.
//

protocol RepositoryProtocol<Item> {
    
    associatedtype Item: Identifiable, Codable
    
    func getAll() async throws -> [Item]
    func getById(_ id: Item.ID) async throws -> Item?
    func insert(_ item: Item) async throws
    func update(_ item: Item) async throws
    func delete(_ item: Item) async throws
    
    
}
