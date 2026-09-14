//
//  RemoteArtistRepository.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/14/26.
//
import Foundation

class RemoteArtistRepository: RepositoryProtocol<Artist>  {
    
    private let urlBase: String
    
    init(urlBase: String) {
        self.urlBase = urlBase
    }
    
    
    func getAll() async throws -> [Artist] {
        
        let urlString = "\(urlBase)/talent"
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        let (data, response) = try await URLSession.shared.data(from: url)
        
        guard let httpResponse = response as? HTTPURLResponse,
              (200..<300).contains(httpResponse.statusCode) else {
            throw NetworkError.invalidResponse
        }
        
        guard let jsonString = String(data: data, encoding: .utf8) else {
            throw NetworkError.invalidResponse
        }
        
        return try JSONDecoder().decode([Artist].self, from: data)
        
    }
    
    func getById(_ id: Int) async throws -> Artist? {
        throw FeatureError.notImplemented
    }
    
    func insert(_ item: Artist) async throws {
        throw FeatureError.notImplemented
    }
    
    func update(_ item: Artist) async throws {
        throw FeatureError.notImplemented
    }
    
    func delete(_ item: Artist) async throws {
        throw FeatureError.notImplemented
    }
    
    
}
